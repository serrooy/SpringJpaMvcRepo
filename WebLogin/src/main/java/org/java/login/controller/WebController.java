package org.java.login.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.java.login.repository.LogLineDao;
import org.java.login.repository.UserDao;
import org.java.login.service.Web;
import org.java.login.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

	public static final String LOG_USU = "logUsu";

	@Autowired
	Web mainService;

	@Autowired
	LogLineDao logLineDao;

	@Autowired
	UserDao userDao;

	/**
	 * Carga la clase por defecto, el index.
	 * 
	 * @return
	 */
	@RequestMapping("/")
	public String init() {

		return Constantes.INDEX;
	}

	/**
	 * Metodo que valida el usuario y la contraseña
	 * 
	 * @param model
	 * @param requestParams
	 * @return
	 * @throws SQLException
	 */
	@RequestMapping(value = "/", method = { RequestMethod.POST })
	public String accionV(Model model, @RequestParam Map<String, String> requestParams) throws SQLException {

		// Login
		if (requestParams.get(Constantes.USERNAME) != null && requestParams.get(Constantes.PASS) != null) {

			String user = requestParams.get(Constantes.USERNAME);
			String pass = requestParams.get(Constantes.PASS);
			model = valUsu(user, pass, model);

		// Consulta log usuario
		} else if (requestParams.get(LOG_USU) != null) {
			String logUsu = requestParams.get(LOG_USU);
			model = getLogUsu(logUsu, model);
		}

		return Constantes.INDEX;
	}

	/**
	 * 
	 * @param logUsu
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	private Model getLogUsu(String logUsu, Model model) throws SQLException {
		List<String> listaLog = mainService.consultaLog(logUsu);
		model.addAttribute(Constantes.LISTA_LOG, listaLog);
		model.addAttribute(Constantes.USUARIO_VALIDO, logUsu);
		model.addAttribute(Constantes.FLAG_LOG, "S");
		model.addAttribute(Constantes.VAROUT, true);
		return model;
	}

	/**
	 * 
	 * @param user
	 * @param pass
	 * @param model
	 * @return
	 * @throws SQLException
	 */
	private Model valUsu(String user, String pass, Model model) throws SQLException {
		boolean result = mainService.validarUser(user, pass);

		if (result) {
			model.addAttribute(Constantes.VAROUT, true);
			model.addAttribute(Constantes.USUARIO_VALIDO, user);

		} else {
			model.addAttribute(Constantes.VAROUT, false);
		}
		return model;
	}

}
