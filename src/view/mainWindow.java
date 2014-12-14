package view;

import model.Model;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class mainWindow extends JFrame {
	private Model model;

	public mainWindow(Model model) {
		this.model = model;
		initUI();
	}

	public void initUI() {
		JPanel panel = new JPanel();
		getContentPane().add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("<html>Лабораторная 1-3<br>По дисцилине: Информационные системы бухгалтерского учета<br><br>Ученика группы ИНФ-12-1<br>Астаппева Олега</html>");
		label.setBounds(20, 20, 600, 440);
		panel.add(label);

		JMenuBar menubar = new JMenuBar();

		JMenu directorySubMenu = new JMenu("Справочник");
		JMenuItem nomenclatureDirectorySubMenu = new JMenuItem("Товары");
		nomenclatureDirectorySubMenu.setToolTipText("Номенклатура");
		nomenclatureDirectorySubMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFrame view = new nomenclatura(model);
				view.setVisible(true);
			}
		});
		directorySubMenu.add(nomenclatureDirectorySubMenu);
		JMenuItem agentProviderDirectorySubMenu = new JMenuItem("Контрагенты - Поставщики");
		agentProviderDirectorySubMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFrame view = new providerAgent(model);
				view.setVisible(true);
			}
		});
		directorySubMenu.add(agentProviderDirectorySubMenu);
		JMenuItem agentBuyerDirectorySubMenu = new JMenuItem("Контрагенты - Покупатели");
		agentBuyerDirectorySubMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFrame view = new buyerAgent(model);
				view.setVisible(true);
			}
		});
		directorySubMenu.add(agentBuyerDirectorySubMenu);
		JMenuItem accountsDirectorySubMenu = new JMenuItem("План счетов");
		accountsDirectorySubMenu.setToolTipText("План счетов, на которых проводится бухгалтерский учет для учета торговых операций и анализа финансовых результатов");
		accountsDirectorySubMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFrame view = new accounts(model);
				view.setVisible(true);
			}
		});
		directorySubMenu.add(accountsDirectorySubMenu);
		JMenuItem baseTransactionDirectorySubMenu = new JMenuItem("Базовые проводки");
		baseTransactionDirectorySubMenu.setToolTipText("Перечень базовых проводок для отображения торговых операций в бухгалтерском учете, вызваных первичными документами");
		baseTransactionDirectorySubMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFrame view = new transactionType(model);
				view.setVisible(true);
			}
		});
		directorySubMenu.add(baseTransactionDirectorySubMenu);

		JMenu documentsSubMenu = new JMenu("Документы");
		JMenuItem addDocumentsSubMenu = new JMenuItem("Провести операцию");
		addDocumentsSubMenu.setToolTipText("Создать проводку по накладной");
		addDocumentsSubMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFrame view = new addOperation(model);
				view.setVisible(true);
			}
		});
		documentsSubMenu.add(addDocumentsSubMenu);
		JMenuItem viewDocumentsSubMenu = new JMenuItem("Журнал операций");
		viewDocumentsSubMenu.setToolTipText("Журнал завершенных операций");
		viewDocumentsSubMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFrame view = new operation(model);
				view.setVisible(true);
			}
		});
		documentsSubMenu.add(viewDocumentsSubMenu);

		JMenu storageSubMenu = new JMenu("Склад");
		JMenuItem storageStorageSubMenu = new JMenuItem("Товары на складе");
		storageStorageSubMenu.setToolTipText("Информация о товарах на складе");
		storageStorageSubMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFrame view = new storage(model);
				view.setVisible(true);
			}
		});
		storageSubMenu.add(storageStorageSubMenu);

		JMenu transactionSubMenu = new JMenu("Проводки");
		JMenuItem transactionTransactionSubMenu = new JMenuItem("Бухгалтерские проводки");
		transactionTransactionSubMenu.setToolTipText("Бухгалтерские провоки вызванные первичными документами");
		transactionTransactionSubMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFrame view = new transaction(model);
				view.setVisible(true);
			}
		});
		transactionSubMenu.add(transactionTransactionSubMenu);

		JMenu reportsSubMenu = new JMenu("Отчеты");
		JMenuItem syntheticReportsSubMenu = new JMenuItem("Ведомость синтетического учета");
		syntheticReportsSubMenu.setToolTipText("Оборотно-сальдовая ведомость синтетического учета");
		syntheticReportsSubMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JFrame view = new report(model);
				view.setVisible(true);
			}
		});
		reportsSubMenu.add(syntheticReportsSubMenu);

		JMenuItem exitItem = new JMenuItem("Выход");
		exitItem.setToolTipText("Зыкрыть программу");
		exitItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		menubar.add(directorySubMenu);
		menubar.add(documentsSubMenu);
		menubar.add(storageSubMenu);
		menubar.add(transactionSubMenu);
		menubar.add(reportsSubMenu);
		menubar.add(Box.createHorizontalGlue());
		menubar.add(exitItem);
		setJMenuBar(menubar);

		BufferedImage image = null;
		try {
			image = ImageIO.read(getClass().getResource("/favicon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		setIconImage(image);
		setTitle("Учет товара");
		setSize(640, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}
}
