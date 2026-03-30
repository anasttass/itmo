package ru.stud;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

public class JsonMaker {
    private final boolean ok;
    private final Object result;
    private final List<HitRecord> history;
    private final String now;
    private final Double execMs;
    private final String error;

    //норм
    public JsonMaker(Object result, List<HitRecord> history, double execMs) {
        this.ok = true;
        this.result = result;
        this.history = history;
        this.now = formatTime(LocalDateTime.now());
        this.execMs = execMs;
        this.error = null;
    }

    //ошибка
    public JsonMaker(String errorMessage) {
        this.ok = false;
        this.result = null;
        this.history = null;
        this.now = formatTime(LocalDateTime.now());
        this.execMs = null;
        this.error = errorMessage;
    }

    public String toJson() {
        if (!ok) {
            return String.format(Locale.US,
                    "{\"ok\":false,\"now\":\"%s\",\"error\":\"%s\"}",
                    now,
                    error.replace("\"", "\\\"")
            );
        }

        StringBuilder historyJson = new StringBuilder("[");
        if (history!=null) {
            for (HitRecord rec : history) {
                historyJson.append(String.format(Locale.US,
                        "{\"x\":%s,\"y\":%s,\"r\":%s,\"hit\":%b,\"time\":\"%s\",\"execNanos\":%.3f},",
                        rec.getX().toPlainString(),
                        rec.getY().toPlainString(),
                        Double.toString(rec.getR()),
                        rec.isHit(),
                        rec.getTime(),
                        rec.getExecNanos() / 1_000_000.0
                ));
            }
        }
        if (historyJson.length() > 1) historyJson.setLength(historyJson.length() - 1);
        historyJson.append("]");

        String resultJson=(result!=null) ? ((ResultData) result).toJson() : "null";

        return String.format(Locale.US,
                "{\"ok\":true,\"result\":%s,\"history\":%s,\"now\":\"%s\",\"execMs\":%.3f}",
                resultJson,
                historyJson,
                now,
                execMs
        );
    }
    private String formatTime(LocalDateTime time) {
        return time.toString().replace("T", " ");
    }
}

