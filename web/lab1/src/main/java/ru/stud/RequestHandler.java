package ru.stud;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class RequestHandler {

    private final AreaChecker checker;
    private final HistoryManager history;

    public RequestHandler() {
        this.checker = new AreaChecker();
        this.history = new HistoryManager();
    }

    public String handle(String queryString) throws ValidationException {

        ParamValidator params = new ParamValidator();
        params.parseAll(queryString);
        BigDecimal x = params.getX();
        BigDecimal y = params.getY();
        double r = params.getR();


        Instant start = Instant.now();
        boolean hit = checker.isHit(x, y, r);
        long execNanos = ChronoUnit.NANOS.between(start, Instant.now());

        HitRecord record = new HitRecord(x, y, r, hit, LocalDateTime.now(), execNanos);
        history.add(record);

        ResultData resultdata= new ResultData(x,y,r,hit);
        JsonMaker json = new JsonMaker(resultdata, history.getAll(), execNanos);
        return json.toJson();

    }
    public String getHistory(){
        try {
            JsonMaker json = new JsonMaker(null, history.getAll(), 0);
            return json.toJson();
        } catch (Exception e) {
            JsonMaker errorJson = new JsonMaker("Error from history");
            return errorJson.toJson();
        }
    }
}



//        StringBuilder historyJson = new StringBuilder("[");
//        for (HitRecord rec : history.getAll()) {
//            historyJson.append(String.format(Locale.US,
//                    "{\"x\":%s,\"y\":%s,\"r\":%s,\"hit\":%b,\"time\":\"%s\",\"execNanos\":%.3f},",
//                    rec.getX().toPlainString(),
//                    rec.getY().toPlainString(),
//                    Double.toString(rec.getR()),
//                    rec.isHit(),
//                    rec.getTime(),
//                    rec.getExecNanos()/1_000_000.0
//            ));
//        }
//        if (historyJson.length() > 1) historyJson.setLength(historyJson.length() - 1);
//        historyJson.append("]");
//
//
//        return String.format(Locale.US, """
//            {
//              "ok": true,
//              "result": {
//                "x": %s,
//                "y": %s,
//                "r": %s,
//                "hit": %b
//              },
//              "history": %s,
//              "now": "%s",
//              "execMs": %.3f
//            }
//            """,
//                x.toPlainString(),
//                y.toPlainString(),
//                Double.toString(r),
//                hit,
//                historyJson,
//                LocalDateTime.now(),
//                execNanos / 1_000_000.0
//        );





