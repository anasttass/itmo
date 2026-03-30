"use strict";

(function () {
    document.addEventListener("DOMContentLoaded", () => {

        // ===== элементы =====
        const svg = document.getElementById("plane-canvas");
        const xHidden = document.getElementById("mainForm:xHidden");
        const yHidden = document.getElementById("mainForm:yHidden");
        const rHidden = document.getElementById("mainForm:rHidden");
        const rSpinnerInput = document.getElementById("mainForm:rSpinner_input");
        const xInput = document.getElementById("mainForm:xInput");
        const rError = document.getElementById("r-error");

        function isFiniteNumber(n) {
            return typeof n === "number" && isFinite(n);
        }

        // ===== отрисовка одной точки =====
        window.drawSvgPoint = function (x, y, r, hit) {
            if (!isFiniteNumber(x) || !isFiniteNumber(y) || !isFiniteNumber(r) || r === 0) return;

            const px = (x / r) * 128;
            const py = (y / r) * 128;

            const circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
            circle.setAttribute("cx", px);
            circle.setAttribute("cy", -py);
            circle.setAttribute("r", "3");
            circle.setAttribute("fill", hit ? "green" : "red");

            svg.appendChild(circle);
        };

        window.clearSvgPoints = function () {
            svg.querySelectorAll("circle").forEach(c => c.remove());
        };

        // ===== перерисовка истории =====
        window.redrawHistoryPoints = function () {
            clearSvgPoints();

            const rows = document.querySelectorAll(".results-table tr");
            rows.forEach(row => {
                const cells = row.querySelectorAll("td");
                if (!cells || cells.length < 4) return;

                const x = parseFloat(cells[0].textContent);
                const y = parseFloat(cells[1].textContent);
                const r = parseFloat(cells[2].textContent);
                const hit = cells[3].textContent.trim() === "Да";

                if (!isFiniteNumber(x) || !isFiniteNumber(y) || !isFiniteNumber(r)) return;
                drawSvgPoint(x, y, r, hit);
            });
        };

        // ===== обновление фигур по R =====
        window.redrawFigures = function (r) {
            if (!isFiniteNumber(r) || r <= 0) return;

            const rect = document.getElementById("rect-area");
            const tri = document.getElementById("triangle-area");
            const circ = document.getElementById("circle-area");

            // прямоугольник (r/2 × r)
            rect.setAttribute("width", (r / 2) * 128);
            rect.setAttribute("height", r * 128);
            rect.setAttribute("y", -r * 128);

            // треугольник (основание r/2)
            tri.setAttribute(
                "points",
                `${-(r / 2) * 128},0 0,0 0,${r * 128}`
            );

            // четверть круга радиуса r/2
            circ.setAttribute(
                "d",
                `M0,0 L0,${(r / 2) * 128} A${(r / 2) * 128},${(r / 2) * 128} 0 0,0 ${(r / 2) * 128},0 Z`
            );
        };

        // ===== клик по графику =====
        svg.addEventListener("click", (e) => {
            const r = parseFloat(rSpinnerInput?.value);

            if (!isFiniteNumber(r) || r < 0.1 || r > 3) {
                if (rError) rError.textContent = "Выберите корректное значение R (0.1–3).";
                return;
            }
            if (rError) rError.textContent = "";

            const rect = svg.getBoundingClientRect();
            const clickX = e.clientX - rect.left;
            const clickY = e.clientY - rect.top;

            const relX = (clickX / rect.width) * 256 - 128;
            const relY = (clickY / rect.height) * 256 - 128;

            const x = (relX / 128) * r;
            const y = -(relY / 128) * r;

            if (xHidden) xHidden.value = x.toFixed(5);
            if (yHidden) yHidden.value = y.toFixed(5);
            if (rHidden) rHidden.value = r;

            if (typeof submitPointFromGraph === "function") {
                submitPointFromGraph();
            } else {
                console.warn("submitPointFromGraph not found");
            }
        });

        // ===== интерактивная проверка X =====
        if (xInput) {
            xInput.addEventListener("input", () => {
                const v = xInput.value.trim();
                if (v === "" || isNaN(Number(v)) || Number(v) < -5 || Number(v) > 5) {
                    xInput.style.borderColor = "red";
                } else {
                    xInput.style.borderColor = "";
                }
            });
        }

        // ===== обработка изменения R =====
        if (rSpinnerInput) {
            rSpinnerInput.addEventListener("change", () => {
                const r = parseFloat(rSpinnerInput.value);

                if (!isFiniteNumber(r) || r < 0.1 || r > 3) {
                    if (rError) rError.textContent = "Некорректное значение R.";
                    return;
                }

                if (rError) rError.textContent = "";
                if (rHidden) rHidden.value = r;

                redrawFigures(r);
                redrawHistoryPoints();
            });
        }

        // ===== начальная отрисовка =====
        setTimeout(() => {
            const r = parseFloat(
                rSpinnerInput?.value ?? rHidden?.value
            );
            if (isFiniteNumber(r)) {
                redrawFigures(r);
            }
            redrawHistoryPoints();
        }, 120);

    });
})();




