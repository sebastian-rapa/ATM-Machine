package ro.sebastianrapa.atmapp.service;

import org.springframework.stereotype.Service;
import ro.sebastianrapa.atmapp.dao.LogDao;
import ro.sebastianrapa.atmapp.model.Log;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    private transient final LogDao logDao;

    public LogServiceImpl(LogDao logDao) {
        this.logDao = logDao;
    }

    @Override
    public List<Log> fetchAllLogs() {
        return logDao.fetchAllLogs();
    }

    @Override
    public void addNewLog(Log log) {
        logDao.addNewLog(log);
    }
}
