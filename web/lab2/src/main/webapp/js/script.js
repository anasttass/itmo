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

// document.addEventListener("DOMContentLoaded", ()=>{
//     const adminbtm = document.getElementById("goadmin");
//     if (adminbtm){
//         adminbtm.addEventListener("click", ()=>{
//             window.location.href="admin.jsp";
//         });
//     }
// })

function checkdata(classname) {
        const checkboxes = document.querySelectorAll(`.${classname}`);
        checkboxes.forEach(cb => {
            cb.addEventListener("change", ()=>{
                if (cb.checked) {
                    checkboxes.forEach(other=>{
                        if (other!=cb) other.checked=false;
                    })
                }
            })
        })
    }

function isDecimalString(s) {
    return /^[+-]?(?:\d+(\.\d*)?|\.\d+)$/.test(s.trim());
}
function checkY(){
    const yInput = document.querySelector('input[name="Y"]');
    if(!yInput)return;

    yInput.addEventListener('blur',function(){
        const value= yInput.value.trim();
        if(!isDecimalString(value)){
            alert('Y должен быть числом');
        }
        yInput.focus();
        return false;
    })
    return true;
}

document.addEventListener("DOMContentLoaded", ()=>{
    checkdata("x-checkbox");
    checkdata("r-checkbox");
    checkY();
})




