package org.java.login.service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.java.login.model.LogLine;
import org.java.login.repository.LogLineDao;
import org.java.login.repository.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Web {

	@Autowired
	LogLineDao logLineDao;

	@Autowired
	UserDao userDao;

	/**
	 * Constructor
	 */
	public Web() {
		super();
	}

	/**
	 * Consulta los logs de un usuario y los formatea
	 * 
	 * @param logUsu
	 * @return
	 */
	public List<String> consultaLog(String logUsu) {
		List<LogLine> listaLog = logLineDao.findByNameLike(logUsu);
		List<String> listaSalida = new ArrayList<>();
		for(LogLine line : listaLog) {
			String rawFecha = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(line.getFechaLog());
			String[] arrFecha = rawFecha.split(" ");

			listaSalida.add("El usuario " + line.getName() + " se conecto el [" + arrFecha[0] + "] a las [" + arrFecha[1]
					+ "]");
		}
		return listaSalida;

	}

	/**
	 * Valida a un usuario
	 * 
	 * @param user
	 * @param pass
	 * @return
	 */

	public boolean validarUser(String user, String pass) {
		if (userDao.findByUserAndPassLike(user, pass) != null) {
			LogLine line = new LogLine();
			line.setName(user);
			line.setFechaLog(new Timestamp(new Date().getTime()));
			logLineDao.save(line);
			return true;
		} else {
			return false;
		}
	}

}
