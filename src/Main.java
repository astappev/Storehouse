import javax.swing.SwingUtilities;

import model.Model;
import controller.Controller;
import view.mainWindow;
import view.providerAgent;

public class Main {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				runApp();
			}

		});
	}

	public static void runApp() {
		Model model = new Model();
		mainWindow view = new mainWindow(model);
		Controller controller = new Controller(view, model);
	}

	public static void providerView() {
		Model model = new Model();
		providerAgent view = new providerAgent(model);
		view.setVisible(true);
	}
}