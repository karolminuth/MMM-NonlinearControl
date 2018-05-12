package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class GeneralController implements Initializable {

	private ChoiceBox<String> choiceBox = new ChoiceBox<String>(
			FXCollections.observableArrayList("Prostokat", "Trojkat", "Sinusoida"));

	private Label tekstWyboruPobudzenia = new Label("  Wybierz\n    opcje\npobudzenia");

	private int amplituda = 25;

	// pobudzenia
	private int arrayProstokat[] = new int[10000];
	private double arrayTrojkat[] = new double[10000];
	private double arraySinusoida[] = new double[10000];

	// parametry z okienek a, A, T
	private double parama;
	private double paramA;
	private double paramT;

	final private NumberAxis xAxis1 = new NumberAxis();
	final private NumberAxis yAxis1 = new NumberAxis();
	final private LineChart<Number, Number> lineChartUchyb = new LineChart<Number, Number>(xAxis1, yAxis1);
	private XYChart.Series series1;

	final private NumberAxis xAxis2 = new NumberAxis();
	final private NumberAxis yAxis2 = new NumberAxis();
	final private LineChart<Number, Number> lineChartWyjscie = new LineChart<Number, Number>(xAxis2, yAxis2);
	private XYChart.Series series2;

	final private NumberAxis xAxis3 = new NumberAxis();
	final private NumberAxis yAxis3 = new NumberAxis();
	final private LineChart<Number, Number> lineChartPobudzenie = new LineChart<Number, Number>(xAxis3, yAxis3);
	private XYChart.Series series3;

	private double dx = 0.01;
	private double calka[] = new double[10000];
	private double x1[] = new double[10000];
	private double x2[] = new double[10000];
	private double uchyb[] = new double[10000];
	private double wartoscU[] = new double[10000];

	@FXML
	private BorderPane borderPane;

	@FXML
	private HBox hBox;

	@FXML
	private VBox vBox1;

	@FXML
	private VBox vBox2;

	@FXML
	private VBox vBox3;

	@FXML
	private Label tekstOpisuPobudzenia;

	@FXML
	private Label labelProstokat;

	@FXML
	private Label labelTrojkat;

	@FXML
	private Label labelSinusoida;

	@FXML
	private ImageView imageProstokat;

	@FXML
	private ImageView imageTrojkat;

	@FXML
	private ImageView imageSinusoida;

	@FXML
	private VBox vBoxDane;

	@FXML
	private Label labela;

	@FXML
	private Label labelA;

	@FXML
	private Label labelT;

	@FXML
	private TextField textFielda;

	@FXML
	private TextField textFieldA;

	@FXML
	private TextField textFieldT;

	@FXML
	private Button buttonUstawDane;

	@FXML
	private VBox vBoxWykresy;

	@FXML
	private VBox vBoxWykresy2;

	public boolean doit = true;

	public ChoiceBox<String> getChoiceBox() {
		return choiceBox;
	}

	public void setChoiceBox(ChoiceBox<String> choiceBox) {
		this.choiceBox = choiceBox;
	}

	private static double funcx1(double x1) {
		return x1;
	}

	private static double funcx2(double x1, double u, double T) {
		return ((-1 / T) * x1 + (1 / T) * u);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		vBoxDane.getChildren().add(0, tekstWyboruPobudzenia);
		vBoxDane.getChildren().add(1, choiceBox);

		// inicjalizowanie tablicy prostokatnej
		for (int i = 0; i < 1000; i++) {
			if (i < 250) {
				arrayProstokat[i] = amplituda;
			} else if (i >= 250 && i < 500) {
				arrayProstokat[i] = -amplituda;
			} else if (i >= 500 && i < 750) {
				arrayProstokat[i] = amplituda;
			} else {
				arrayProstokat[i] = -amplituda;
			}
		}

		// inicjalizowanie tabicy trojkatnej
		double a1 = 0;
		for (int i = 0; i < 1000; i++) {
			if (i < 250) {
				arrayTrojkat[i] = a1;
				a1 += 0.1;
			} else if (i >= 250 && i < 500) {
				a1 -= 0.1;
				arrayTrojkat[i] = a1;
			} else if (i >= 500 && i < 750) {
				a1 -= 0.1;
				arrayTrojkat[i] = a1;
			} else {
				a1 += 0.1;
				arrayTrojkat[i] = a1;
			}
		}

		// inicjalizowanie tablicy sinus
		double czestotliwosc = 0.0;
		for (int i = 0; i < 1000; i++) {
			if (i < 250) {
				arraySinusoida[i] = amplituda * Math.sin(czestotliwosc * Math.PI);
				czestotliwosc += 0.002;
			} else if (i >= 250 && i < 500) {
				czestotliwosc -= 0.002;
				arraySinusoida[i] = amplituda * Math.sin(czestotliwosc * Math.PI);
			} else if (i >= 500 && i < 750) {
				czestotliwosc -= 0.002;
				arraySinusoida[i] = amplituda * Math.sin(czestotliwosc * Math.PI);
			} else {
				czestotliwosc += 0.002;
				arraySinusoida[i] = amplituda * Math.sin(czestotliwosc * Math.PI);
			}
		}

		////// tu jest caly kod
		buttonUstawDane.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				try {
					parama = Double.parseDouble(textFielda.getText());
				} catch (Exception e) {
					e.printStackTrace();
					textFielda.setText("Podaj liczbe");
				}

				try {
					paramA = Double.parseDouble(textFieldA.getText());
				} catch (Exception e) {
					e.printStackTrace();
					textFieldA.setText("Podaj liczbe");
				}

				try {
					paramT = Double.parseDouble(textFieldT.getText());
				} catch (Exception e) {
					e.printStackTrace();
					textFieldT.setText("Podaj liczbe");
				}
				// te trzy to wartosc z okienek do zmiennych

				if (doit) {
					series1 = new XYChart.Series();
					series2 = new XYChart.Series();
					series3 = new XYChart.Series();
					vBoxWykresy.getChildren().add(0, lineChartPobudzenie);
					vBoxWykresy.getChildren().add(1, lineChartUchyb);
					vBoxWykresy.getChildren().add(2, lineChartWyjscie);
					doit = false;
				}

				String wyborPobudzenia = choiceBox.getValue();

				if (wyborPobudzenia.equals("Prostokat")) {

					try {
						double r[] = new double[1001];

						for (int i = 0; i < 1001; i++) {
							r[i] = arrayProstokat[i];
						}
						// rysowanie pobudzenia
						series3.getData().clear();
						series3.setName("Pobudzenie");
						for (int i = 0; i < 1000; i++) {
							series3.getData().add(new XYChart.Data<Integer, Integer>(i, arrayProstokat[i]));
						}
						lineChartPobudzenie.getData().clear();
						lineChartPobudzenie.getData().add(series3);

						// warunki poczatkowe
						x1[0] = 0;
						x2[0] = 0;
						uchyb[0] = 0;
						double x1suma = 0;
						double x2suma = 0;
						double usuma = 0;

						for (int i = 1; i <= 1000; i++) {

							uchyb[i] = r[i] - x1[i - 1];

							if (r[i - 1] < parama || r[i - 1] > -parama) {
								wartoscU[i] = r[i];
							} else if (r[i - 1] < -parama) {
								wartoscU[i] = -parama;
							} else
								wartoscU[i] = parama;

							x1[i] = x2suma;
							x2[i] = -(1 / paramT) * x1suma + (1 / paramT) * usuma;

							x1suma = x1suma + dx * x1[i];
							x2suma = x2suma + dx * x2[i];
							usuma = usuma + dx * wartoscU[i];

						}

						// rysowanie uchybu
						series1.getData().clear();
						series1.setName("Uchyb1");
						for (int i = 0; i < 1001; i++) {
							series1.getData().add(new XYChart.Data<Integer, Double>(i, uchyb[i]));
						}
						lineChartUchyb.getData().clear();
						lineChartUchyb.getData().add(series1);

						// rysowanie wyjscia
						series2.getData().clear();
						series2.setName("Wyjœcie");
						for (int i = 0; i < 1001; i++) {
							series2.getData().add(new XYChart.Data<Integer, Double>(i, x1[i]));
						}
						lineChartWyjscie.getData().clear();
						lineChartWyjscie.getData().add(series2);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}

				} else if (wyborPobudzenia.equals("Trojkat")) {
					try {

						double r[] = new double[1001];

						for (int i = 0; i < 1001; i++) {
							r[i] = arrayTrojkat[i];
						}
						////////////////////////////////////////////////////////////
						// rysowanie pobudzenia
						series3.getData().clear();
						series3.setName("Pobudzenie");
						for (int i = 0; i < 1000; i++) {
							series3.getData().add(new XYChart.Data<Integer, Double>(i, arrayTrojkat[i]));
						}
						lineChartPobudzenie.getData().clear();
						lineChartPobudzenie.getData().add(series3);
						////////////////////////////////////////////////////////////

						// warunki poczatkowe
						x1[0] = 0;
						x2[0] = 0;
						uchyb[0] = 0;
						double x1suma = 0;
						double x2suma = 0;
						double usuma = 0;

						for (int i = 1; i <= 1000; i++) {

							uchyb[i] = r[i] - x1[i - 1];

							if (r[i - 1] < parama || r[i - 1] > -parama) {
								wartoscU[i] = r[i];
							} else if (r[i - 1] < -parama) {
								wartoscU[i] = -parama;
							} else
								wartoscU[i] = parama;

							x1[i] = x2suma;
							x2[i] = -(1 / paramT) * x1suma + (1 / paramT) * usuma;

							x1suma = x1suma + dx * x1[i];
							x2suma = x2suma + dx * x2[i];
							usuma = usuma + dx * wartoscU[i];

						}

						// rysowanie uchybu
						series1.getData().clear();
						series1.setName("Uchyb2");
						for (int i = 0; i < 1001; i++) {
							series1.getData().add(new XYChart.Data<Integer, Double>(i, uchyb[i]));
						}
						lineChartUchyb.getData().clear();
						lineChartUchyb.getData().add(series1);

						// rysowanie wyjscia
						series2.getData().clear();
						series2.setName("Wyjœcie");
						for (int i = 0; i < 1001; i++) {
							series2.getData().add(new XYChart.Data<Integer, Double>(i, x1[i]));
						}
						lineChartWyjscie.getData().clear();
						lineChartWyjscie.getData().add(series2);

					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}

				} else if (wyborPobudzenia.equals("Sinusoida")) {

					try {

						double r[] = new double[1001];

						for (int i = 0; i < 1001; i++) {
							r[i] = arraySinusoida[i];
						}
						////////////////////////////////////////////////////////////
						// rysowanie pobudzenia
						series3.getData().clear();
						series3.setName("Pobudzenie");
						for (int i = 0; i < 1000; i++) {
							series3.getData().add(new XYChart.Data<Integer, Double>(i, arraySinusoida[i]));
						}
						lineChartPobudzenie.getData().clear();
						lineChartPobudzenie.getData().add(series3);
						////////////////////////////////////////////////////////////

						// warunki poczatkowe
						x1[0] = 0;
						x2[0] = 0;
						uchyb[0] = 0;
						double x1suma = 0;
						double x2suma = 0;
						double usuma = 0;

						for (int i = 1; i <= 1000; i++) {

							uchyb[i] = r[i] - x1[i - 1];

							if (r[i - 1] < parama || r[i - 1] > -parama) {
								wartoscU[i] = r[i];
							} else if (r[i - 1] < -parama) {
								wartoscU[i] = -parama;
							} else
								wartoscU[i] = parama;

							x1[i] = x2suma;
							x2[i] = -(1 / paramT) * x1suma + (1 / paramT) * usuma;

							x1suma = x1suma + dx * x1[i];
							x2suma = x2suma + dx * x2[i];
							usuma = usuma + dx * wartoscU[i];

						}

						// rysowanie uchybu
						series1.getData().clear();
						series1.setName("Uchyb3");
						for (int i = 0; i < 1001; i++) {
							series1.getData().add(new XYChart.Data<Integer, Double>(i, uchyb[i]));
						}
						lineChartUchyb.getData().clear();
						lineChartUchyb.getData().add(series1);

						// rysowanie wyjscia
						series2.getData().clear();
						series2.setName("Wyjœcie");
						for (int i = 0; i < 1001; i++) {
							series2.getData().add(new XYChart.Data<Integer, Double>(i, x1[i]));
						}
						lineChartWyjscie.getData().clear();
						lineChartWyjscie.getData().add(series2);

					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					}

				} else
					System.out.println("Blad przy porownywaniu obiektow");
			}

		});
		
		
		buttonUstawDane.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {

			public void handle(ActionEvent event) {

			}
		});
	}
}
