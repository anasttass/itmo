'use strict'

//переходы и проверки
document.addEventListener("DOMContentLoaded", ()=>{
    const btm = document.getElementById("goback");
    if (btm){
        btm.addEventListener("click", ()=>{
            window.location.href="index.jsp";
        });
    }
})

document.addEventListener("DOMContentLoaded", ()=>{
    function checkdata(classname) {
        const checkboxes = document.querySelectorAll(`.{classname}`);
        checkboxes.forEach(cb => {
            scrollBy.addEventListener("change", ()=>{
                if (cb.checked) {
                    checkboxes.forEach(other=>{
                        if (other!=cb) other.checked=false;
                    })
                }
            })
        })
    }
})

checkdata("x-checkbox");
checkdata("r-checkbox");

