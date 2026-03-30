package ru.stud;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ParamValidator {
    private BigDecimal x;
    private BigDecimal y;
    private double r;


    public void parseAll(String query) throws ValidationException {
        if (query == null || query.isBlank()) {
            throw new ValidationException("Empty query string");
        }
        Map<String, String> params = splitQuery(query);
        this.x = parseX(params.get("x"));
        this.y = parseY(params.get("y"));
        this.r = parseR(params.get("r"));
    }


    private Map<String, String> splitQuery(String query) {
        return Arrays.stream(query.split("&"))
                .map(pair -> pair.split("=", 2))
                .collect(Collectors.toMap(
                        p -> URLDecoder.decode(p[0], StandardCharsets.UTF_8),
                        p -> p.length > 1 ? URLDecoder.decode(p[1], StandardCharsets.UTF_8) : "",
                        (a, b) -> b,
                        HashMap::new
                ));
    }

    private BigDecimal parseX(String raw) throws ValidationException {
        if (raw == null || raw.isBlank()) throw new ValidationException("x missing");
        try {
            BigDecimal val = new BigDecimal(raw.trim());
            if (val.compareTo(BigDecimal.valueOf(-3)) < 0 || val.compareTo(BigDecimal.valueOf(5)) > 0) {
                throw new ValidationException("x out of range [-3,5]");
            }
            return val.stripTrailingZeros();
        } catch (NumberFormatException e) {
            throw new ValidationException("x not decimal");
        }
    }


    private BigDecimal parseY(String raw) throws ValidationException {
        if (raw == null || raw.isBlank()) throw new ValidationException("y missing");
        try {
            BigDecimal val = new BigDecimal(raw.trim());
            if (val.compareTo(BigDecimal.valueOf(-3)) < 0 || val.compareTo(BigDecimal.valueOf(5)) > 0) {
                throw new ValidationException("y out of range [-3,5]");
            }
            return val.stripTrailingZeros();
        } catch (NumberFormatException e) {
            throw new ValidationException("y not decimal");
        }
    }

    private double parseR(String raw) throws ValidationException {
        if (raw == null || raw.isBlank()) {
            throw new ValidationException("r missing");
        }
        try {
            double val = Double.parseDouble(raw.trim());
            if (val != 1.0 && val != 1.5 && val != 2.0 && val != 2.5 && val != 3.0) {
                throw new ValidationException("r must be one of [1,1.5,2,2.5,3]");
            }
            return val;
        } catch (NumberFormatException e) {
            throw new ValidationException("r not double");
        }
    }

    public BigDecimal getX() {
        return x;
    }
    public BigDecimal getY() {
        return y;
    }
    public double getR() {
        return r;
    }
}
