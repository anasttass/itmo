"use strict";


const PATH_FCGI = "control";
const resultDiv = document.getElementById("result");
const svg = document.getElementById("plane-canvas");
//const hitCircle = document.getElementById("hit-circle");
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

    let px = centerX + (nx / nr) * scale;
    let py = centerY - (ny / nr) * scale;

    
    const pad = 4; 
    const minX = pad, maxX = vb.width - pad;
    const minY = pad, maxY = vb.height - pad;
    if (px < minX) px = minX;
    if (px > maxX) px = maxX;
    if (py < minY) py = minY;
    if (py > maxY) py = maxY;

    const hitCircle = document.createElementNS("http://www.w3.org/2000/svg", "circle");//создаем теперь новую точку прям при рисовке
    hitCircle.setAttribute("r", 4);
    hitCircle.setAttribute("cx", px);
    hitCircle.setAttribute("cy", py);
    hitCircle.setAttribute("fill", hit ? "#00ff88" : "#ff4444");
    svg.appendChild(hitCircle);
}



function sendPoint(x,y,r){
    const form = document.createElement('form');
    form.method='POST';
    form.action='control';

    ['X','Y','R'].forEach((name,i)=>{
        const input = document.createElement('input');
        input.type='hidden';
        input.name=name;
        input.value=[x,y,r][i];
        form.appendChild(input);
    });
    document.body.appendChild(form);
    form.submit();
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

    if (x > 3 || x<-5 || Math.abs(y) > 5) {
        alert("Координаты вне диапазона");
        return;
    }

    sendPoint(x,y.toString(),r);
});



