package ro.sebastianrapa.atmapp.dao;

import org.springframework.stereotype.Repository;
import ro.sebastianrapa.atmapp.model.Log;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LogDaoFakeImpl implements LogDao{

    private List<Log> FAKE_LOG_DB = new ArrayList<>();

    @Override
    public List<Log> fetchAllLogs() {
        return FAKE_LOG_DB;
    }

    @Override
    public void addNewLog(Log log) {
        FAKE_LOG_DB.add(log);
    }
}
