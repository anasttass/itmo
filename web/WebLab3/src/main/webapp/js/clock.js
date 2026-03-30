function updateClock(){
    const now = new Date();
    const hours = String(now.getHours()).padStart(2,"0");
    const minutes = String(now.getMinutes()).padStart(2,"0");
    const seconds = String(now.getSeconds()).padStart(2,"0");
    const date = now.toLocaleDateString('ru-RU');
    document.getElementById('clock').textContent=`${hours}:${minutes}:${seconds}    ${date}`;
}

window.onload= function(){
    updateClock();
    this.setInterval(updateClock, 5000);
}