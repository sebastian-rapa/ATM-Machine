package ro.sebastianrapa.atmapp.dao;

import ro.sebastianrapa.atmapp.model.Log;

import java.util.List;

public interface LogDao {

    List<Log> fetchAllLogs();

    void addNewLog(Log log);
}
