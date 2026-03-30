package ru.stud.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Vetoed;
import jakarta.inject.Inject;
import org.jooq.DSLContext;
import ru.stud.model.ResultHolder;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static ru.stud.jooq.tables.Results.RESULTS;

@ApplicationScoped
@Jooq
public class JooqResultRepository implements IResultRepository{

    @Inject
    private DSLContext dsl;

    public JooqResultRepository() {}

    @Override
    public ResultHolder save(ResultHolder r) {
        var record = dsl.insertInto(RESULTS)
                .set(RESULTS.X,r.getX())
                .set(RESULTS.Y,r.getY())
                .set(RESULTS.R,r.getR())
                .set(RESULTS.HIT,r.getHit())
                .set(RESULTS.TIME,r.getTime())
                .returning()
                .fetchOne();

        return jooqToMyEntity(record);
    }

    @Override
    public List<ResultHolder> listAll() {
         return dsl.selectFrom(RESULTS)
                .orderBy(RESULTS.TIME.desc())
                .fetch()
                .map(this::jooqToMyEntity);
    }

    @Override
    public void clearAll() {
        dsl.deleteFrom(RESULTS).execute();
    }

    private ResultHolder jooqToMyEntity(org.jooq.Record record){
        if (record==null){
            return null;
        }
        ResultHolder rh = new ResultHolder();
        rh.setId(record.get(RESULTS.ID));
        rh.setX(record.get(RESULTS.X));
        rh.setY(record.get(RESULTS.Y));
        rh.setR(record.get(RESULTS.R));
        rh.setHit(record.get(RESULTS.HIT));
        rh.setTime(record.get(RESULTS.TIME));

        return rh;
    }
}
