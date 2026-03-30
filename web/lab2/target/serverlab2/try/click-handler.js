"use strict";


const PATH_FCGI = "control";

const resultDiv = document.getElementById("result");
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

function isDecimalString(s) {
    return /^[+-]?(?:\d+(\.\d*)?|\.\d+)$/.test(s.trim());
}

async function sendToServer(xVal, yRaw, rVal) {
    const requestData = {
        x: String(xVal),
        y: String(yRaw),
        r: String(rVal)
    };
    
    //console.log("DEBUG: Отправляю POST запрос", requestData);
    
    try {
        const resp = await fetch(PATH_FCGI, { 
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
                "X-Requested-With": "XMLHttpRequest"
            },
            body: new URLSearchParams(requestData).toString(),
            cache: "no-store" 
        });
        
        //console.log("DEBUG: Получен ответ", resp.status);
        
        const text = await resp.text();

        if (!resp.ok) {
            try {
                const errData = JSON.parse(text);
                const userMsg = errData.error 
                    ? `Ошибка: ${errData.error}` 
                    : `Ошибка сервера (${resp.status})`;
                resultDiv.textContent = userMsg;
                return; 
            } catch {
                resultDiv.textContent = `Ошибка сервера (${resp.status})`;
                return;
            }
        }

        let responseData;
        try {
            responseData = JSON.parse(text);
        } catch {
            throw new Error("Server returned non-JSON response");
        }

        if (!responseData.ok) {
            throw new Error("Ошибка обработки: " + (responseData.error || JSON.stringify(responseData)));
        }

        const res = responseData.result || {};

        if (res.x !== undefined && res.y !== undefined && res.r !== undefined) {
            drawPoint(res.x, res.y, res.r, res.hit);
        }

        resultDiv.textContent = `Точка (${res.x}, ${res.y}), R=${res.r} — ${res.hit ? "Попадает" : "Мимо"})`;


    } catch (err) {
        console.error("Fetch/send error:", err);
        resultDiv.textContent = "Fetch error: " + err.message;
    }
}

svg.addEventListener("click", function(event) {
    const rInput = document.querySelector(`input[name="R"]:checked`);
    if (!rInput) {
        alert("Сначала выберите R!");
        return;
    }
    const r = parseFloat(rInput.value);

    const pt = svg.createSVGPoint();
    pt.x = event.clientX;
    pt.y = event.clientY;
    const ctm = svg.getScreenCTM().inverse(); 
    const svgPoint = pt.matrixTransform(ctm);

    const vb = svg.viewBox.baseVal;
    const centerX = vb.width / 2;
    const centerY = vb.height / 2;

    const x = (svgPoint.x - centerX) * r / scale;
    const y = (centerY - svgPoint.y) * r / scale;

    if (Math.abs(x) > 5 || Math.abs(y) > 5) {
        alert("Координаты вне диапазона [-5;5]");
        return;
    }

    sendToServer(x, String(y), r);
});

//console.log("Все обработчики установлены!");
