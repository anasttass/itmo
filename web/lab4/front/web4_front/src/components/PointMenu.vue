<template>
    <table class="maintable">
        <tr>
            <td>
                <div class="card">
                    <form id="data-form" @submit.prevent="checkPoint()">
                        <table>
                            <tr>
                                <td><label for="x">Выберите X:</label></td>
                                <td>
                                    <label v-for="x in xs" :key="x">
                                        <input type="radio" name="x" :value="x" v-model="selectedX">{{ x }}</input>
                                    </label>
                                </td>
                            </tr>
                            <tr>
                                <td><label for="y">Выберите Y:</label></td>
                                <td><input type="text" placeholder="Введите y" v-model="y"></input></td>
                            </tr>
                            <tr>
                                <td><label>Выберите R:</label></td>
                                <td>
                                    <label v-for="r in rs" :key="r">
                                        <input type="radio" name="r" :value="r" v-model="selectedR">{{ r }}</input>
                                    </label>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2" align="center">
                                    <button type="submit">Проверить</button>
                                </td>
                            </tr>
                        </table>
                    </form>

                    <h3>{{ message }}</h3>
                    <button @click="goBack">Закончить сессию и вернуться назад</button>
                </div>
            </td>

            <td width="50%" align="center">
                <div class="card"> Координатная плоскость
                    <svg id="plane-canvas" viewBox="0 0 512 384" xmlns="http://www.w3.org/2000/svg" @click="onSvgClick">
                        <!-- Области -->
                        <g id="plane-areas" fill="rgba(0,150,255,0.3)" stroke="#70d6ff" stroke-width="1">
                            <!-- треугольник (0,-R)(-R/2,0) -->
                            <polygon points="256,192 224,192 256,256" />

                            <!-- окружность -->
                            <path d="M256,192 L224,192 A64,64 0 0,1 256,160 Z" />

                            <!-- прямоугольник (0,R/2)(R,0) -->
                            <rect x="256" y="160" width="64" height="32" />
                        </g>

                        <!-- Оси -->
                        <g stroke="#fff" stroke-width="1">
                            <line x1="0" y1="192" x2="512" y2="192" />
                            <line x1="256" y1="0" x2="256" y2="384" />
                            <text x="500" y="182" fill="#fff">x</text>
                            <text x="264" y="12" fill="#fff">y</text>
                        </g>

                        <!-- Деления -->
                        <g fill="#fff" font-size="12" text-anchor="middle">
                            <text x="192" y="182">-R</text>
                            <text x="224" y="182">-R/2</text>
                            <text x="288" y="182">R/2</text>
                            <text x="320" y="182">R</text>
                            <text x="264" y="128">R</text>
                            <text x="264" y="160">R/2</text>
                            <text x="264" y="224">-R/2</text>
                            <text x="264" y="256">-R</text>
                        </g>
                        <g id="points">
                            <circle
                                v-for="(p, index) in history"
                                :key="index"
                                :cx="svgX(p.x, selectedR)"
                                :cy="svgY(p.y, selectedR)"
                                r="4"
                                :fill="p.hit ? '#00ff88' : '#ff4444'"
                            />
                        </g>
                    </svg>

                </div>
            </td>
        </tr>

        <tr>
            <td colspan="2" align="center">
                <div class="card">
                    <h2 id="memory-title">История</h2>
                    <table id="memory">
                        <tr>
                            <td>X</td>
                            <td>Y</td>
                            <td>R</td>
                            <td>Hit</td>
                            <td>Time</td>
                        </tr>
                        <tr v-for="(point, index) in history" :key="index">
                            <td>{{ point.x }}</td>
                            <td>{{ point.y }}</td>
                            <td>{{ point.r }}</td>
                            <td>{{ point.hit }}</td>
                            <td>{{ point.time }}</td>
                        </tr>
                    </table>
                    <button @click="clearHistory()">Очистить историю</button>
                </div>
            </td>
        </tr>
    </table>
</template>

<script>
import router from '@/router/router';

    export default{
        mounted(){
            this.getHistory();
        },
        data(){
            return{
                xs: [-4,-3,-2,-1,0,1,2,3,4],
                rs: [1,2,3,4],
                selectedX: null,
                y: "",
                selectedR: 1,
                message: "",
                history:[]
            }
        },
        methods:{
            async checkPoint(){
                if (this.selectedR==null || this.y=="" || this.selectedX==null){
                    this.message="Все поля должны быть заполнены!";
                    return;
                }
                this.message="";
                this.sendPoint(this.selectedX,this.y);
            },

            async clearHistory(){
                const resp = await fetch("http://localhost:8081/server4/app/control/clear",{
                    credentials: "include",
                    method: "DELETE"
                })
                if(resp.status==401){
                    router.push("/login")
                }
                this.getHistory();
                this.message="";
            },

            async getHistory(){
                const resp = await fetch("http://localhost:8081/server4/app/control/history",{
                    method: "GET",
                    credentials: "include",
                })
                if (resp.ok){
                    const points = await resp.json();
                    this.history=points;
                }
                else if(resp.status==401){
                    router.push("/login")
                }
                else{
                    const data = await resp.text();
                    this.message = data;
                }
            },
            svgX(x, r) {
                const centerX = 256;
                const scale = 64;
                return centerX + (x / r) * scale;
                },

            svgY(y, r) {
                const centerY = 192;
                const scale = 64;
                return centerY - (y / r) * scale;
            },
            onSvgClick(event) {
                if (!this.selectedR) {
                    this.message = "Сначала выберите R";
                    return;
                }

                const svg = event.currentTarget;
                const pt = svg.createSVGPoint();

                pt.x = event.clientX;
                pt.y = event.clientY;

                const cursor = pt.matrixTransform(svg.getScreenCTM().inverse());

                const centerX = 256;
                const centerY = 192;
                const scale = 64;

                const x = ((cursor.x - centerX) / scale) * this.selectedR;
                const y = ((centerY - cursor.y) / scale) * this.selectedR;

                this.sendPoint(x, y);
            },

            async sendPoint(x, y) {
                this.message="";
                const resp = await fetch("http://localhost:8081/server4/app/control/check", {
                    method: "POST",
                    credentials: "include",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({x, y, r: this.selectedR})
                });

                if (resp.ok) {
                    const point = await resp.json();
                    this.history.push(point);
                } 
                else if(resp.status==401){
                    router.push("/login")
                }
                else {
                    const data = await resp.text();
                    this.message = data;
                }
            },
            async goBack(){
                // localStorage.removeItem("auth");
                const resp = await fetch("http://localhost:8081/server4/app/control/logout", {
                    method: "POST",
                    credentials: "include",
                    headers: { "Content-Type": "application/json" },
                });
                this.$router.push("/login")
            }
        }
    }
</script>

<style>
label {
    display: inline-flex;
    align-items: center;
    margin-right: 0.5rem;
    font-size: 0.9rem;
}

@media(max-width:733px){
    .maintable tr{
        display: block;
    }

    /* .maintable td{
        display: block;
        width:100%;
    } */
    .maintable td[width="50%"]{
        width: 100%;
        display: block;
    }
    .maintable{
        margin-right: 10px;
    }
}

</style>
