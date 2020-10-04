package ro.sebastianrapa.atmapp.service;

import ro.sebastianrapa.atmapp.model.Log;

import java.util.List;

public interface LogService {

    void addNewLog(Log log);

    List<Log> fetchAllLogs();
}
