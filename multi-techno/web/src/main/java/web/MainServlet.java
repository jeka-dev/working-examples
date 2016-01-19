package web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.common.primitives.Ints;

import core.MyCore;

public class MainServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private final MyCore core = new MyCore();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String input = req.getParameter("input");
		if (input == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "'input' resquest parameter not found.");
			return;
		}
		Integer in = Ints.tryParse(input);
		if (in == null) {
			resp.sendError(422, "Input number should be an integer.");
			return;
		}
		int result = core.magicFormula(in);
		resp.setContentType("text/plain");
		resp.getWriter().append(Integer.toString(result));
	}

}
