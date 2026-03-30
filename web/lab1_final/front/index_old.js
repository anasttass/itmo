"use strict";

console.log("✅ JavaScript загружен и выполняется!");
const PATH_FCGI = "http://localhost:8081/fcgi-bin/server.jar";
//const PossibleXs = new Set([-3,-2,-1,0,1,2,3,4,5]);
//const possibleRs = new Set([1,1.5,2,2.5,3]);

const form = document.getElementById("data-form");
const resultDiv = document.getElementById("result");
const memoryDiv = document.getElementById("memory");
const svg = document.getElementById("plane-canvas");
const hitCircle = document.getElementById("hit-circle");
//const center_x = 256;
//const center_y=192;
const scale=64;
const max_coord=5;

function drawPoint(x, y, r, hit) {
    const nx = Number(x);
    const ny = Number(y);
    const nr = Number(r);
    if (!isFinite(nx) || !isFinite(ny) || !isFinite(nr) || nr === 0) return;

    const vb = svg.viewBox.baseVal;
    const centerX = vb.width / 2;
    const centerY = vb.height / 2;

    // px = center + (x / r) * scale
    let px = centerX + (nx / nr) * scale;
    let py = centerY - (ny / nr) * scale;

    
    const pad = 4; 
    const minX = pad, maxX = vb.width - pad;
    const minY = pad, maxY = vb.height - pad;
    if (px < minX) px = minX;
    if (px > maxX) px = maxX;
    if (py < minY) py = minY;
    if (py > maxY) py = maxY;

    hitCircle.setAttribute("cx", px);
    hitCircle.setAttribute("cy", py);
    hitCircle.setAttribute("fill", hit ? "#00ff88" : "#ff4444");
}



function isDecimalString(s){
    if (typeof s !== "string") return false;
    s = s.trim();
    return /^[+-]?(?:\d+(\.\d*)?|\.\d+)$/.test(s); //вернула \.\d+ слэш после точки
}

function compareDecimalSrtings(a,b){
    const norm = (x) =>{
        x=x.trim();
        let sign = 1;
        if (x[0]==='+' || x[0]==='-'){
            if (x[0]==='-') sign=-1;
            x=x.slice(1);
        }
        if(x==="")x="0";

        const [intPartRaw, fracPartRaw=""]=x.split(".");

        let intPart = intPartRaw.replace(/^0+/,"");
        if (intPart==="") intPart="0";

        let fracPart = fracPartRaw.replace(/0+$/,"");
        return {sign, intPart, fracPart};
    }

    const A = norm(a);
    const B = norm(b);
    //при разных знаках
    if (A.sign!== B.sign) return A.sign < B.sign ? -1:1;
    //длинна целой части
    if (A.intPart.length !== B.intPart.length) {
        const cmp = A.intPart.length < B.intPart.length ? -1 : 1;
        return A.sign === 1 ? cmp : -cmp;
    }
    //по лексикографическому
    if (A.intPart !== B.intPart) {
        const cmp = A.intPart < B.intPart ? -1 : 1;
        return A.sign === 1 ? cmp : -cmp;
    }
    //сравнение дробных плюс нули
    const maxFrac = Math.max(A.fracPart.length, B.fracPart.length);
    const aFrac = A.fracPart.padEnd(maxFrac, "0");
    const bFrac = B.fracPart.padEnd(maxFrac, "0");
    if (aFrac !== bFrac) {
        const cmp = aFrac < bFrac ? -1 : 1;
        return A.sign === 1 ? cmp : -cmp;
    }
    return 0;
}
    
function isYInRange(yStr){
    if(!isDecimalString(yStr)) return false;
    if (compareDecimalSrtings(yStr,"-3")<0) return false;
    if (compareDecimalSrtings(yStr, "5")>0) return false;
    return true;
}

function renderHistoryRows(history){
    let html = `<table border="1" cellpadding="4" cellspacing="0">
    <tr><th>x</th><th>y</th><th>r</th><th>hit</th><th>time</th><th>execMs</th></tr>`;
    for (const row of history){
        const rx = row.x ?? "";
        const ry = (row.y !== undefined)? String(row.y):"";
        const rr = row.r ?? "";
        const rh = (row.hit === true) ? "Попадание":"Мимо";
        const rt = row.time ?? "";
        const re = row.execNanos ?? "";
        html+=`<tr>
            <td>${rx}</td>
            <td>${ry}</td>
            <td>${rr}</td>
            <td>${rh}</td>
            <td>${rt}</td>
            <td>${re}</td>
        </tr>`
    }
    html += "</table>";
    return html;
}

async function sendToServer(xVal, yRaw, rVal) {
    // const params = new URLSearchParams();
    // params.append("x", String(xVal));
    // params.append("y", String(yRaw));
    // params.append("r", String(rVal));
    //const url = `${PATH_FCGI}?${params.toString()}`;

    const requestData = {
        x: String(xVal),
        y: String(yRaw),
        r: String(rVal)
    };
    console.log("🔍 DEBUG: Отправляю POST запрос", {
        url: PATH_FCGI,
        method: "POST", 
        data: requestData,
        body: new URLSearchParams(requestData).toString()
    });
    try {
        const resp = await fetch(PATH_FCGI, { method: "POST",headers: {"Content-Type": "application/x-www-form-urlencoded",},body:new URLSearchParams(requestData).toString(),cache: "no-store" });
        const text = await resp.text();

        if (!resp.ok) {
            try {
                const errData = JSON.parse(text);
                const userMsg = errData.error 
                    ? `Ошибка: ${errData.error}` 
                    : `Ошибка сервера (${resp.status})`;
                resultDiv.textContent = userMsg;
                console.warn("Server returned error:", errData);
                return; 
            } catch {
                resultDiv.textContent = `Ошибка сервера (${resp.status})`;
                console.warn("Server returned non-JSON error:", text);
                return;
            }
        }

        let data;
        try {
            data = JSON.parse(text);
        } catch {
            throw new Error("Server returned non-JSON response:\n" + text);
        }

        if (!data.ok) {
            throw new Error("Ошибка обработки: " + (data.error || JSON.stringify(data)));
        }

        const res = data.result || {};

        if (res.x !== undefined && res.y !== undefined && res.r !== undefined) {
            drawPoint(res.x, res.y, res.r, res.hit);
        }

        resultDiv.textContent = `Точка (${res.x}, ${res.y}), R=${res.r} — ${res.hit ? "Попадает" : "Мимо"})`// (время: ${data.now}, exec: ${data.execMs} ms)`;

        const history = data.history || [];
        memoryDiv.innerHTML = renderHistoryRows(history);

    } catch (err) {
        console.error("Fetch/send error:", err);
        resultDiv.textContent = "Fetch error: " + err.message;
    }
}


form.addEventListener("submit",function(event){
    event.preventDefault();

    const xVal = document.getElementById("x").value;
    const yVal = document.getElementById("y").value.trim();
    const rVal = document.querySelector(`input[name="r"]:checked`);

    if (yVal === "" || !isDecimalString(yVal)){
        resultDiv.textContent="Ошибка. Y должен быть числом.";
        return;
    }
    if (!isYInRange(yVal)){
        resultDiv.textContent="Ошибка. Y вне диапазона [-3;5].";
        return;
    }
    if (!rVal){
        resultDiv.textContent="Ошибка. Выберите R.";
        return
    }
    const x = Number(xVal);
    const r = Number(rVal.value);

    sendToServer(x,yVal,r);

});

svg.addEventListener("click", function(event) {
    const rInput = document.querySelector(`input[name="r"]:checked`);
    if (!rInput) {
        alert("Сначала выберите R!");
        return;
    }
    const r = parseFloat(rInput.value);

    //координаты окна в svg:
    const pt = svg.createSVGPoint();
    pt.x = event.clientX;
    pt.y = event.clientY;
    const ctm = svg.getScreenCTM().inverse(); 
    const svgPoint = pt.matrixTransform(ctm);

    //центр и шкала из viewBox 
    const vb = svg.viewBox.baseVal;
    const centerX = vb.width / 2;
    const centerY = vb.height / 2;

    //x = (px - center) * r / scale
    const x = (svgPoint.x - centerX) * r / scale;
    const y = (centerY - svgPoint.y) * r / scale;

    //console.log(`Click on plane: SVG=${svgPoint.x.toFixed(1)},${svgPoint.y.toFixed(1)} -> X=${x.toFixed(3)}, Y=${y.toFixed(3)}, R=${r}`);

    sendToServer(x, String(y), r);
});
