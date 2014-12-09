package controller;

import view.mainWindow;
import model.Model;

public class Controller {
	private mainWindow view;
	private Model model;

	public Controller(mainWindow view, Model model) {
		this.view = view;
		this.model = model;
		view.setVisible(true);
	}
}
