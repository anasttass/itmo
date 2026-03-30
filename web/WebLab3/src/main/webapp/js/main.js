"use strict";


//const max_coord=5;
//const resultDiv = document.getElementById("result");
const svg = document.getElementById("plane-canvas");
const scale=64;

//отрисовка точки
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

//обновление всех точек
function updateAllPoints(){
    if(!svg){return;}
    svg.querySelectorAll("circle").forEach(c=>c.remove());

    const rows = document.querySelectorAll("#historyTable tbody tr");

    const rInput = document.getElementById("mainForm:r_input"); //один последний радиус для всех
    const r = parseFloat(rInput?.value.replace(',','.'));

    rows.forEach(row => {
        const cells = row.querySelectorAll("td");

        const x = parseFloat(cells[0].innerText);
        const y = parseFloat(cells[1].innerText);
        const hit = cells[3].innerText.includes("Попадает");

        if (r==null || !r){
            const newr = parseFloat(cells[2].innerText); //если r не выбран то берем обычный
            drawPoint(x,y,r,hit);
            return;
        }
        drawPoint(x,y,r,hit)
    })
}

const rInput = document.getElementById("mainForm:r_input")
//флаг для лисенера
let clickHandlerInitialized = false;
//обработка клика
function initClickEvents(){
    if (clickHandlerInitialized){
        return;
    }
    clickHandlerInitialized=true;
    svg.addEventListener("click", function(event) {
        const r = parseFloat(rInput?.value.replace(',','.'));
        if (!r || isNaN(r)) {
            alert("Сначала выберите R!");
            return;
        }

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

        if (x > 4 || x<-4 || Math.abs(y) > 5) {
            alert("Координаты вне диапазона");
            return;
        }

        document.getElementById("mainForm:xHidden").value=x;
        document.getElementById("mainForm:yHidden").value=y;

        PrimeFaces.ab({
            source:'mainForm:clickSubmit',
            process: '@form',
            update: 'historyTable :areaPanel mainForm:messages',
            oncomplete: function() {
                updateAllPoints();
                setTimeout(convertAllTableTimes, 50);
            }
        });
    });
}

//обновляем при загрузке
document.addEventListener("DOMContentLoaded", function(){
    updateAllPoints();
    initClickEvents();
    rChanges();
})


function rChanges(){
    if (rInput){
        rInput.addEventListener("input", updateAllPoints);
    }

    const spinner = document.getElementById("mainForm:r");

    const up=spinner.querySelector(".ui-spinner-up");
    const down=spinner.querySelector(".ui-spinner-down");

    if(up){
        up.addEventListener("click", ()=> {setTimeout(updateAllPoints(),0)})
    }
    if(down){
        down.addEventListener("click", ()=> {setTimeout(updateAllPoints(),0)})
    }
}

//адаптация под часовые пояса
function convertMoscowToUserTime(moscowTimeStr) {
    try {

        const [datePart, timePart] = moscowTimeStr.split(' ');
        const [day, month, year] = datePart.split('.');
        const [hours, minutes, seconds] = timePart.split(':');
        
        const tempDate = new Date(Date.UTC(
            parseInt(year), 
            parseInt(month) - 1, 
            parseInt(day),
            parseInt(hours) - 3, 
            parseInt(minutes), 
            parseInt(seconds)
        ));
        
        const userTimezone = Intl.DateTimeFormat().resolvedOptions().timeZone;
        
        const userTime = tempDate.toLocaleString('ru-RU', {
            timeZone: userTimezone,
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit',
            hour12: false
        });
        
        return userTime;
        
    } catch (error) {
        console.error('Ошибка конвертации времени:', error);
        return moscowTimeStr + ' (МСК)';
    }
}

function convertAllTableTimes() {
    const timeElements = document.querySelectorAll('.time-value');
    
    timeElements.forEach(element => {
        const originalTime = element.textContent.trim();
        const userTime = convertMoscowToUserTime(originalTime);
        element.textContent = userTime;
        
    });
}
document.addEventListener('DOMContentLoaded', function() {
    setTimeout(convertAllTableTimes, 100);
});

