
import org.junit.Test;
import ru.stud.model.DtoResultHolder;
import ru.stud.service.HitChecker;

import static org.junit.Assert.*;

public class HitTests {
    private final HitChecker checker = new HitChecker();


    @Test
    public void checkTriangle(){
        DtoResultHolder point = new DtoResultHolder();
        point.setX(Double.parseDouble("0.999"));
        point.setY(Double.parseDouble("0"));
        point.setR(Double.parseDouble("1"));
        assertTrue(checker.isHit(point));
    }

    @Test
    public void checkNotInTriangle(){
        DtoResultHolder point = new DtoResultHolder();
        point.setX(Double.parseDouble("1.1"));
        point.setY(Double.parseDouble("0"));
        point.setR(Double.parseDouble("1"));
        assertFalse(checker.isHit(point));
    }


    @Test
    public void checkCircle(){
        DtoResultHolder point = new DtoResultHolder();
        point.setX(Double.parseDouble("1.5"));
        point.setY(Double.parseDouble("-0.75"));
        point.setR(Double.parseDouble("3"));
        assertTrue(checker.isHit(point));
    }


    @Test
    public void checkEmpty(){
        DtoResultHolder point = new DtoResultHolder();
        point.setX(Double.parseDouble("-2"));
        point.setY(Double.parseDouble("2"));
        point.setR(Double.parseDouble("1"));
        assertFalse(checker.isHit(point));
    }
}
