package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Controller implements Initializable {


    @FXML
    private TableColumn<Part, Integer> partsCol;

    @FXML
    private TableColumn<Part, ?> imgCol;

    @FXML
    private TextField txtName, txtLego_used, txtCreate_used, txtParts_to_use, txtParent, txtCategory, txtSubcategory;

    @FXML
    private AnchorPane mainPane, opacityPane, drawerPane, partPane;

    @FXML
    private GridPane grCategories;

    @FXML
    private ImageView imageView2, imgviewDel1, imgviewDel2, imgviewDel3, imgviewDel4, imgviewDel5, imgviewDel6,
            imgviewDel7, imgviewDel8, imgviewDel9;

    @FXML
    private Label lblName, lblImage, lblImageDisplay, lblLego_used, lblCreate_used, lbbParts_to_use, lblParent,
            lblCategory, lblSubcategory;

    @FXML
    private RadioButton btnLego;

    @FXML
    private RadioButton btnCreate;


    @FXML
    private Button btnAdd_Part, btnChooseImage, btnDelete_Part, brnAdd, btnLoad, btnDelete, btnMenu1, btnMenu2, btnMenu3, btnMenu4, btnMenu5,
            btnMenu6, btnMenu7, btnMenu8, btnMenu9,
             btnAdd, btnBack, btnAddToDb;


    @FXML
    private TableColumn<Part, Integer> legoCol;

    @FXML
    private TableColumn<Part, String> nameCol;

    @FXML
    private TableView<Part> tableParts;

    @FXML
    private TableColumn<Part, Integer> createCol;

    private ObservableList<Part> data;

    ArrayList<String> auxiliary = new ArrayList<>();
    ArrayList<String> parents = new ArrayList<>();
    ArrayList<String> categories = new ArrayList<>();
    ArrayList<String> subcategories = new ArrayList<>();
    ArrayList<String> buttonsClicked = new ArrayList<>();
    ArrayList<Integer> dataId = new ArrayList<>();
    String lastClicked = "";
    int maxAuxiliarySize = 3;
    int errorCounter = 0;
    int imageIndicator = 0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ArrayList<Button> menu = new ArrayList<>();
        {
            menu.add(btnMenu1);
            menu.add(btnMenu2);
            menu.add(btnMenu3);
            menu.add(btnMenu4);
            menu.add(btnMenu5);
            menu.add(btnMenu6);
            menu.add(btnMenu7);
            menu.add(btnMenu8);
            menu.add(btnMenu9);
        }

        ArrayList<ImageView> crossImages = new ArrayList<>();
        {
            crossImages.add(imgviewDel1);
            crossImages.add(imgviewDel2);
            crossImages.add(imgviewDel3);
            crossImages.add(imgviewDel4);
            crossImages.add(imgviewDel5);
            crossImages.add(imgviewDel6);
            crossImages.add(imgviewDel7);
            crossImages.add(imgviewDel8);
            crossImages.add(imgviewDel9);
        }

        partPane.setVisible(false);
        opacityPane.setVisible(false);

        drawerPane.setVisible(true);
        displayMenu();
        displayButtonsOnKeyReleased();
        displayButtonsOnMouseClicked();
        crossVisibility();


        btnBack.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(partPane.isVisible() == false)
                goBack();
            }
        });

        btnAdd.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                addToLegoOrCreate();
            }
        });

        btnDelete.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                deleteFromLegoOrCreate();
            }
        });


            for (ImageView imgview : crossImages)
                imgview.addEventFilter(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        if(partPane.isVisible() == false)
                        crossImageAction(menu.get(crossImages.indexOf(imgview)));
                    }
                });


        mainPane.addEventFilter(javafx.scene.input.KeyEvent.KEY_RELEASED, new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                if (partPane.isVisible() == false) {
                    switch (event.getCode()) {
                        case BACK_SPACE:
                            goBack();
                            break;
                        case A:
                            addToLegoOrCreate();
                            break;
                        case PLUS:
                            addToLegoOrCreate();
                            break;
                        case D:
                            deleteFromLegoOrCreate();
                            break;
                        case MINUS:
                            deleteFromLegoOrCreate();
                            break;
                        case LEFT:
                            if (btnCreate.isSelected()) {
                                btnLego.setSelected(true);
                            }
                            break;
                        case RIGHT:
                            if (btnLego.isSelected()) {
                                btnCreate.setSelected(true);
                            }


                    }

                }
            }
        });

        txtLego_used.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                String c = event.getCharacter();
                char ch = c.charAt(0);

                if (!(Character.isDigit(ch) || (ch == KeyEvent.VK_BACK_SPACE) || ch == KeyEvent.VK_DELETE)) {
                    event.consume();

                }
            }
        });

        txtCreate_used.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                String c = event.getCharacter();
                char ch = c.charAt(0);

                if (!(Character.isDigit(ch) || (ch == KeyEvent.VK_BACK_SPACE) || ch == KeyEvent.VK_DELETE)) {

                    event.consume();

                }
            }
        });

        txtParts_to_use.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, new EventHandler<javafx.scene.input.KeyEvent>() {
            @Override
            public void handle(javafx.scene.input.KeyEvent event) {
                String c = event.getCharacter();
                char ch = c.charAt(0);

                if (!(Character.isDigit(ch) || (ch == KeyEvent.VK_BACK_SPACE) || ch == KeyEvent.VK_DELETE)) {
                    event.consume();

                }
            }
        });

        opacityPane.setOnMouseClicked(event -> {
            partPane.setVisible(false);
            opacityPane.setVisible(false);

        });

        btnAdd_Part.setOnMouseClicked(event -> {
            opacityPane.setVisible(true);
            partPane.setVisible(true);

        });

    }

    @FXML
    void addToLegoOrCreate() {
        int index = tableParts.getSelectionModel().getFocusedIndex();
        if (index >= 0) {
            tableParts.setEditable(true);
            TableColumn legoCol = tableParts.getColumns().get(2);
            TableColumn createCol = tableParts.getColumns().get(3);

            String name = nameCol.getCellObservableValue(index).getValue();
            ImageView image = (ImageView) imgCol.getCellObservableValue(index).getValue();
            int lego_used = (int) legoCol.getCellObservableValue(index).getValue();
            int create_used = (int) createCol.getCellObservableValue(index).getValue();
            int parts_to_use = (int) partsCol.getCellObservableValue(index).getValue();

            int lego = (int) legoCol.getCellObservableValue(index).getValue();
            int create = (int) createCol.getCellObservableValue(index).getValue();

            if (btnLego.isSelected()) {
                if (lego_used + create_used < parts_to_use) {
                    data.set(index, new Part(name, image, lego_used + 1, create_used, parts_to_use));

                    String UpdateQuery = null;
                    PreparedStatement ps = null;
                    Connection con = getConnection();

                    try {
                        UpdateQuery = "UPDATE parts SET lego_used = ? WHERE id ='" + dataId.get(index) + "'";
                        ps = con.prepareStatement(UpdateQuery);

                        ps.setInt(1, lego + 1);

                        ps.executeUpdate();

                        tableParts.getSelectionModel().select(index);

                    } catch (SQLException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Too many parts in use!");
                }
            } else {
                if (lego_used + create_used < parts_to_use) {
                    data.set(index, new Part(name, image, lego_used, create_used + 1, parts_to_use));

                    String UpdateQuery = null;
                    PreparedStatement ps = null;
                    Connection con = getConnection();

                    try {
                        UpdateQuery = "UPDATE parts SET create_used = ? WHERE id ='" + dataId.get(index) + "'";
                        ps = con.prepareStatement(UpdateQuery);

                        ps.setInt(1, create + 1);

                        ps.executeUpdate();

                        tableParts.getSelectionModel().select(index);

                    } catch (SQLException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Too many parts in use!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Select part from the table");
        }
    }

    @FXML
    void deleteFromLegoOrCreate() {
        int index = tableParts.getSelectionModel().getFocusedIndex();
        if (index >= 0) {
            tableParts.setEditable(true);
            TableColumn legoCol = tableParts.getColumns().get(2);
            TableColumn createCol = tableParts.getColumns().get(3);

            String name = (String) nameCol.getCellObservableValue(index).getValue();
            ImageView image = (ImageView) imgCol.getCellObservableValue(index).getValue();
            int lego_used = (int) legoCol.getCellObservableValue(index).getValue();
            int create_used = (int) createCol.getCellObservableValue(index).getValue();
            int parts_to_use = (int) partsCol.getCellObservableValue(index).getValue();

            int lego = (int) legoCol.getCellObservableValue(index).getValue();
            int create = (int) createCol.getCellObservableValue(index).getValue();

            if (btnLego.isSelected()) {
                if (lego_used > 0) {
                    data.set(index, new Part(name, image, lego_used - 1, create_used, parts_to_use));

                    String UpdateQuery = null;
                    PreparedStatement ps = null;
                    Connection con = getConnection();

                    try {
                        UpdateQuery = "UPDATE parts SET lego_used = ? WHERE id ='" + dataId.get(index) + "'";
                        ps = con.prepareStatement(UpdateQuery);

                        ps.setInt(1, lego - 1);

                        ps.executeUpdate();

                        tableParts.getSelectionModel().select(index);

                    } catch (SQLException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Lego_used cannot be negative!");
                }
            } else {
                if (create_used > 0) {
                    data.set(index, new Part(name, image, lego_used, create_used - 1, parts_to_use));

                    String UpdateQuery = null;
                    PreparedStatement ps = null;
                    Connection con = getConnection();

                    try {
                        UpdateQuery = "UPDATE parts SET create_used = ? WHERE id ='" + dataId.get(index) + "'";
                        ps = con.prepareStatement(UpdateQuery);

                        ps.setInt(1, create - 1);

                        ps.executeUpdate();

                        tableParts.getSelectionModel().select(index);

                    } catch (SQLException ex) {
                        Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Create_used cannot be negative!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Select part from the table");
        }
    }

    public Connection getConnection() {
        Connection con = null;


        try {
           con = DriverManager.getConnection("jdbc:mysql://sql7.freesqldatabase.com/sql7277061", "sql7277061", "HEnkh1WcIN");

           /* con = DriverManager.getConnection("jdbc:mysql://localhost/robotics", "root", ""); */
              /* JOptionPane.showMessageDialog(null, "Connected"); */

            return con;

        } catch (SQLException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }


    }

    public ArrayList<String> getParents() {
        ArrayList<String> allParents = new ArrayList<>();
        HashSet<String> set = new HashSet<String>();
        ArrayList<String> parents = new ArrayList<>();

        try {
            Connection con = getConnection();

            ResultSet rs = con.createStatement().executeQuery("SELECT parent FROM tree ");
            while (rs.next()) {
                String k = rs.getString("parent");
                allParents.add(k);
            }

            set = new HashSet<String>(allParents);
            parents = new ArrayList<String>(set);
            con.close();

        } catch (SQLException ex) {
            System.out.println("Error" + ex);
        }
        return parents;
    }

    public ArrayList<String> getCategories(String parent) {
        ArrayList<String> allCategories = new ArrayList<>();
        HashSet<String> set = new HashSet<String>();
        ArrayList<String> categories = new ArrayList<>();

        try {
            Connection con = getConnection();

            ResultSet rs = con.createStatement().executeQuery("SELECT category FROM tree WHERE parent = '" + parent + "'");
            while (rs.next()) {
                String k = rs.getString("category");
                allCategories.add(k);
            }

            set = new HashSet<String>(allCategories);
            categories = new ArrayList<String>(set);
            con.close();

        } catch (SQLException ex) {
            System.out.println("Error" + ex);
        }
        return categories;
    }

    public ArrayList<String> getSubcategories(String parent, String category) {
        ArrayList<String> allSubcategories = new ArrayList<>();
        HashSet<String> set = new HashSet<String>();
        ArrayList<String> subcategories = new ArrayList<>();

        try {
            Connection con = getConnection();

            ResultSet rs = con.createStatement().executeQuery("SELECT subcategory FROM tree WHERE parent = '" +
                    parent + "' AND category = '" + category + "'");
            while (rs.next()) {
                String k = rs.getString("subcategory");
                allSubcategories.add(k);
            }

            set = new HashSet<String>(allSubcategories);
            subcategories = new ArrayList<String>(set);
            con.close();

        } catch (SQLException ex) {
            System.out.println("Error" + ex);
        }
        return subcategories;
    }


    public void loadDataFromDb(String parent, String category, String subcategory) {
        try {
            dataId.clear();
            Connection con = getConnection();
            data = FXCollections.observableArrayList();
            ArrayList<InputStream> is = new ArrayList<>();


            ResultSet rs = con.createStatement().executeQuery("SELECT parts.* FROM parts, tree WHERE " +
                    "tree.id=parts.id AND parent='" + parent + "' AND category='" + category + "' AND subcategory='" +
                    subcategory + "'");

            while (rs.next()) {
                is.add(rs.getBinaryStream(3));
                ArrayList<ImageView> convertedImages = new ArrayList<>();
                convertedImages.add(InputStreamToImageView(is.get(is.size() - 1)));
                data.add(new Part(rs.getString(2), convertedImages.get(0), rs.getInt(4), rs.getInt(5), rs.getInt(6)));
                dataId.add(rs.getInt(1));

            }
            is.clear();
            con.close();
            System.out.println("Connection closed");

        } catch (SQLException ex) {
            System.out.println("Error" + ex);
        }

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        imgCol.setCellValueFactory(new PropertyValueFactory<>("image"));
        legoCol.setCellValueFactory(new PropertyValueFactory<>("lego_used"));
        createCol.setCellValueFactory(new PropertyValueFactory<>("create_used"));
        partsCol.setCellValueFactory(new PropertyValueFactory<>("parts_to_use"));

        tableParts.setItems(null);
        tableParts.setItems(data);
        tableParts.getSelectionModel().select(0);


    }

    public ImageView InputStreamToImageView(InputStream is) {
        try {
            OutputStream os = new FileOutputStream(new File("photo.png"));
            byte[] content = new byte[1024];
            int size = 0;
            while ((size = is.read(content)) != -1) {
                os.write(content, 0, size);
            }
            os.close();
            is.close();


        } catch (FileNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);

        } catch (IOException exc) {
            System.out.println("IO Exception");
        }
        Image image = new Image("file:photo.png", 100, 150, true, true);
        ImageView imageView = new ImageView(image);

        return imageView;


    }


    @FXML
    void addToDb(ActionEvent event) {
        ArrayList<Button> menu = new ArrayList<>();
        {
            menu.add(btnMenu1);
            menu.add(btnMenu2);
            menu.add(btnMenu3);
            menu.add(btnMenu4);
            menu.add(btnMenu5);
            menu.add(btnMenu6);
            menu.add(btnMenu7);
            menu.add(btnMenu8);
            menu.add(btnMenu9);
        }

        errorCounter = 0;
        errorCheck();
        if(errorCounter == 0){
            imageIndicator =0;
        }
        if(errorCounter == 0){


        try {
            Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("INSERT INTO parts(parts.name , image, lego_used, create_used, parts_to_use)" +
                    "values(?,?,?,?,?)");

            ps.setString(1, txtName.getText());

            ps.setBlob(2, imageToInputStream(imageView2));
            System.out.println(txtLego_used.getText());

            ps.setInt(3, Integer.parseInt(txtLego_used.getText()));

            System.out.println(txtCreate_used.getText());
            ps.setInt(4, Integer.parseInt(txtCreate_used.getText()));

            System.out.println(txtParts_to_use.getText());
            ps.setInt(5, Integer.parseInt(txtParts_to_use.getText()));

            ps.executeUpdate();

            PreparedStatement ps1 = con.prepareStatement("INSERT INTO tree( parent, category, subcategory)" +
                    "values(?,?,?)");

            ps1.setString(1, txtParent.getText());


            ps1.setString(2, txtCategory.getText());

            ps1.setString(3, txtSubcategory.getText());

            ps1.executeUpdate();

            JOptionPane.showMessageDialog(null, "Data");
            con.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage() + "error");

        }
            if (buttonsClicked.size() == 0) {
                auxiliary.remove(auxiliary.size()-1);
                parents = getParents();
                for (Button btnd : menu) {
                    showOnDrawerButton(btnd, parents, categories, subcategories);
                }
                auxiliary.add("");
            }

            if (buttonsClicked.size() == 1) {
                auxiliary.remove(auxiliary.size()-1);
                categories = getCategories(buttonsClicked.get(0));
                for (Button btnd : menu) {
                    showOnDrawerButton(btnd, parents, categories, subcategories);
                }
                auxiliary.add("");
            }
            if (buttonsClicked.size() == 2 ) {
                auxiliary.remove(auxiliary.size()-1);
                subcategories = getSubcategories(buttonsClicked.get(0),buttonsClicked.get(1));
                for (Button btnd : menu) {
                    showOnDrawerButton(btnd, parents, categories, subcategories);
                }
                auxiliary.add("");
            }

    }

}

    public boolean errorCheck() {
        errorCounter = 0;
        txtName.getStyleClass().remove("error");
        txtLego_used.getStyleClass().remove("error");
        txtCreate_used.getStyleClass().remove("error");
        txtParts_to_use.getStyleClass().remove("error");
        txtParent.getStyleClass().remove("error");

        if (txtName.getText().trim().equals("")) {
            txtName.getStyleClass().add("error");
            errorCounter += 1;
        }
        if(imageIndicator == 0){
            JOptionPane.showMessageDialog(null, "Add image!!");
            errorCounter += 1;
        }

        if (txtLego_used.getText().trim().equals("")) {
            txtLego_used.getStyleClass().add("error");
            errorCounter += 1;
        }
        if (txtCreate_used.getText().trim().equals("")) {
            txtCreate_used.getStyleClass().add("error");
            errorCounter += 1;
        }
        if (txtParts_to_use.getText().trim().equals("")) {
            txtParts_to_use.getStyleClass().add("error");
            errorCounter += 1;
        }
        if (txtParent.getText().trim().equals("")) {
            txtParent.getStyleClass().add("error");
            errorCounter += 1;
        }


        if (errorCounter > 0)
            return true;
        else {
            return false;
        }

    }


    @FXML
    void chooseImage(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Part Picture");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*jpg"));
        File selectedFile = fileChooser.showOpenDialog(null);
        File selectedFileInput = selectedFile;

        Image image5 = new Image(selectedFile.toURI().toString());
        imageView2.setImage(image5);
        imageIndicator += 1;
    }



    public InputStream imageToInputStream(ImageView imageView) {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 0;
            }
        };
        BufferedImage bImage = SwingFXUtils.fromFXImage(imageView2.getImage(), null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bImage, "png", outputStream);
            byte[] res = outputStream.toByteArray();
            inputStream = new ByteArrayInputStream(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputStream;

    }

    public void displayMenu() {
        ArrayList<Button> menu = new ArrayList<>();
        {
            menu.add(btnMenu1);
            menu.add(btnMenu2);
            menu.add(btnMenu3);
            menu.add(btnMenu4);
            menu.add(btnMenu5);
            menu.add(btnMenu6);
            menu.add(btnMenu7);
            menu.add(btnMenu8);
            menu.add(btnMenu9);
        }

        auxiliary.add("");
        parents = getParents();

        for (Button btn : menu) {

            for (String par : parents) {
                menu.get(parents.indexOf(par)).setText(par);
            }
            if (menu.indexOf(btn) >= parents.size()) {
                menu.get(menu.indexOf(btn)).setVisible(false);
            }
        }

    }


    public void displayButtonsOnKeyReleased() {
        ArrayList<Button> menu = new ArrayList<>();
        {
            menu.add(btnMenu1);
            menu.add(btnMenu2);
            menu.add(btnMenu3);
            menu.add(btnMenu4);
            menu.add(btnMenu5);
            menu.add(btnMenu6);
            menu.add(btnMenu7);
            menu.add(btnMenu8);
            menu.add(btnMenu9);
        }

        mainPane.setOnKeyReleased(e -> {

            switch (e.getCode()) {


                case DIGIT1:
                    if (btnMenu1.isVisible() && partPane.isVisible() == false) {
                        buttonsClicked.add(btnMenu1.getText());
                        whatToGetAndLoadFromDB();

                        for (Button btnd : menu) {
                            showOnDrawerButton(btnd, parents, categories, subcategories);
                        }
                        if (auxiliary.size() < maxAuxiliarySize) {
                            auxiliary.add("");
                        } else {
                            buttonsClicked.remove(buttonsClicked.size() - 1);
                        }

                    }
                    break;
                case DIGIT2:
                    if (btnMenu2.isVisible() && partPane.isVisible() == false) {
                        buttonsClicked.add(btnMenu2.getText());
                        whatToGetAndLoadFromDB();

                        for (Button btnd : menu) {
                            showOnDrawerButton(btnd, parents, categories, subcategories);
                        }
                        if (auxiliary.size() < maxAuxiliarySize) {
                            auxiliary.add("");
                        } else {
                            buttonsClicked.remove(buttonsClicked.size() - 1);
                        }
                    }

                    break;
                case DIGIT3:
                    if (btnMenu3.isVisible() && partPane.isVisible() == false) {
                        buttonsClicked.add(btnMenu3.getText());
                        whatToGetAndLoadFromDB();


                        for (Button btnd : menu) {
                            showOnDrawerButton(btnd, parents, categories, subcategories);
                        }
                        if (auxiliary.size() < maxAuxiliarySize) {
                            auxiliary.add("");
                        } else {

                            buttonsClicked.remove(buttonsClicked.size() - 1);
                        }
                    }

                    break;
                case DIGIT4:
                    if (btnMenu4.isVisible() && partPane.isVisible() == false) {
                        buttonsClicked.add(btnMenu4.getText());
                        whatToGetAndLoadFromDB();

                        for (Button btnd : menu) {

                            showOnDrawerButton(btnd, parents, categories, subcategories);
                        }
                        if (auxiliary.size() < maxAuxiliarySize) {
                            auxiliary.add("");
                        } else {
                            buttonsClicked.remove(buttonsClicked.size() - 1);
                        }
                    }

                    break;
                case DIGIT5:
                    if (btnMenu5.isVisible() && partPane.isVisible() == false) {
                        buttonsClicked.add(btnMenu5.getText());
                        whatToGetAndLoadFromDB();

                        for (Button btnd : menu) {
                            showOnDrawerButton(btnd, parents, categories, subcategories);
                        }
                        if (auxiliary.size() < maxAuxiliarySize) {
                            auxiliary.add("");
                        } else {
                            buttonsClicked.remove(buttonsClicked.size() - 1);
                        }
                    }

                    break;
                case DIGIT6:
                    if (btnMenu6.isVisible() && partPane.isVisible() == false) {
                        buttonsClicked.add(btnMenu6.getText());

                        if (auxiliary.size() == 3) {
                            loadDataFromDb(buttonsClicked.get(buttonsClicked.size() - 3), buttonsClicked.get(buttonsClicked.size() - 2), buttonsClicked.get(buttonsClicked.size() - 1));
                        }

                        if (buttonsClicked.size() == 0)
                            parents = getParents();
                        if (buttonsClicked.size() == 1) {
                            categories = getCategories(buttonsClicked.get(buttonsClicked.size() - 1));
                            if (categories.get(0) == "")
                                loadDataFromDb(buttonsClicked.get(buttonsClicked.size() - 1), "", "");
                        }
                        if (buttonsClicked.size() == 2) {
                            subcategories = getSubcategories(buttonsClicked.get(buttonsClicked.size() - 2), buttonsClicked.get(buttonsClicked.size() - 1));
                            if (subcategories.get(0) == "")
                                loadDataFromDb(buttonsClicked.get(buttonsClicked.size() - 2), buttonsClicked.get(buttonsClicked.size() - 1), "");

                        }
                        for (Button btnd : menu) {
                            showOnDrawerButton(btnd, parents, categories, subcategories);
                        }
                        if (auxiliary.size() < 3) {
                            auxiliary.add("");
                        } else {
                            buttonsClicked.remove(buttonsClicked.size() - 1);
                        }
                    }

                    break;
                case DIGIT7:
                    if (btnMenu7.isVisible() && partPane.isVisible() == false) {
                        buttonsClicked.add(btnMenu7.getText());

                        if (auxiliary.size() == 3) {
                            loadDataFromDb(buttonsClicked.get(buttonsClicked.size() - 3), buttonsClicked.get(buttonsClicked.size() - 2), buttonsClicked.get(buttonsClicked.size() - 1));
                        }

                        if (buttonsClicked.size() == 0)
                            parents = getParents();
                        if (buttonsClicked.size() == 1) {
                            categories = getCategories(buttonsClicked.get(buttonsClicked.size() - 1));
                            if (categories.get(0) == "")
                                loadDataFromDb(buttonsClicked.get(buttonsClicked.size() - 1), "", "");
                        }
                        if (buttonsClicked.size() == 2) {
                            subcategories = getSubcategories(buttonsClicked.get(buttonsClicked.size() - 2), buttonsClicked.get(buttonsClicked.size() - 1));
                            if (subcategories.get(0) == "")
                                loadDataFromDb(buttonsClicked.get(buttonsClicked.size() - 2), buttonsClicked.get(buttonsClicked.size() - 1), "");

                        }
                        for (Button btnd : menu) {
                            showOnDrawerButton(btnd, parents, categories, subcategories);
                        }
                        if (auxiliary.size() < maxAuxiliarySize) {
                            auxiliary.add("");
                        } else {
                            buttonsClicked.remove(buttonsClicked.size() - 1);
                        }
                    }

                    break;
                case DIGIT8:
                    if (btnMenu8.isVisible() && partPane.isVisible() == false) {
                        buttonsClicked.add(btnMenu8.getText());
                        whatToGetAndLoadFromDB();

                        for (Button btnd : menu) {
                            showOnDrawerButton(btnd, parents, categories, subcategories);
                        }
                        if (auxiliary.size() < maxAuxiliarySize) {
                            auxiliary.add("");
                        } else {
                            buttonsClicked.remove(buttonsClicked.size() - 1);
                        }
                    }

                    break;
                case DIGIT9:
                    if (btnMenu9.isVisible() && partPane.isVisible() == false) {
                        buttonsClicked.add(btnMenu9.getText());
                        whatToGetAndLoadFromDB();

                        for (Button btnd : menu) {
                            showOnDrawerButton(btnd, parents, categories, subcategories);
                        }
                        if (auxiliary.size() < maxAuxiliarySize) {
                            auxiliary.add("");
                        } else {
                            buttonsClicked.remove(buttonsClicked.size() - 1);
                        }
                    }

                    break;

            }
        });
    }

    public void whatToGetAndLoadFromDB() {

              if(auxiliary.size() == 3) {
                  loadDataFromDb(buttonsClicked.get(buttonsClicked.size() - 3), buttonsClicked.get(buttonsClicked.size() - 2), buttonsClicked.get(buttonsClicked.size() - 1));
            }

            if (buttonsClicked.size() == 0)
                parents = getParents();
            if (buttonsClicked.size() == 1) {
                categories = getCategories(buttonsClicked.get(buttonsClicked.size() - 1));
                if(categories.get(0) == "")
                    loadDataFromDb(buttonsClicked.get(buttonsClicked.size() - 1), "","");
            }
            if (buttonsClicked.size() == 2) {
                subcategories = getSubcategories(buttonsClicked.get(buttonsClicked.size() - 2), buttonsClicked.get(buttonsClicked.size() - 1));
                if(subcategories.get(0) == "")
                    loadDataFromDb(buttonsClicked.get(buttonsClicked.size() - 2), buttonsClicked.get(buttonsClicked.size() - 1), "");
            }

    }



    public void showOnDrawerButton(Button btn, ArrayList<String> parents, ArrayList<String> categories, ArrayList<String> subcategories) {
        ArrayList<Button> menu = new ArrayList<>();
        {
            menu.add(btnMenu1);
            menu.add(btnMenu2);
            menu.add(btnMenu3);
            menu.add(btnMenu4);
            menu.add(btnMenu5);
            menu.add(btnMenu6);
            menu.add(btnMenu7);
            menu.add(btnMenu8);
            menu.add(btnMenu9);
        }
        int index = menu.indexOf(btn);

        switch (auxiliary.size()) {
            case 0:
                if (index < parents.size()) {
                    btn.setVisible(true);
                    btn.setText(parents.get(index));
                } else {
                    btn.setVisible(false);
                }

                break;
            case 1:
                if (index < categories.size() && !(categories.get(0) == "")) {
                    btn.setVisible(true);
                    btn.setText(categories.get(index));
                } else {
                    btn.setVisible(false);
                }

                break;
            case 2:
                if (index < subcategories.size() && !(subcategories.get(0) == "")) {
                    btn.setVisible(true);
                    btn.setText(subcategories.get(index));
                } else {
                    btn.setVisible(false);
                }

                break;

        }
        crossVisibility();

    }




    public void displayButtonsOnMouseClicked() {
        parents = getParents();
        if(buttonsClicked.size() >= 1)
            categories = getCategories(buttonsClicked.get(buttonsClicked.size() - 1));
        if(buttonsClicked.size() >=2)
            subcategories = getSubcategories(buttonsClicked.get(buttonsClicked.size() - 2), buttonsClicked.get(buttonsClicked.size() - 1));

        ArrayList<Button> drawer = new ArrayList<>();
        {
            drawer.add(btnMenu1);
            drawer.add(btnMenu2);
            drawer.add(btnMenu3);
            drawer.add(btnMenu4);
            drawer.add(btnMenu5);
            drawer.add(btnMenu6);
            drawer.add(btnMenu7);
            drawer.add(btnMenu8);
            drawer.add(btnMenu9);
        }

        for (Button btn : drawer) {
            btn.setOnMouseClicked(e -> {
                if(btn.isVisible() && partPane.isVisible() == false) {
                    buttonsClicked.add(btn.getText());
                    whatToGetAndLoadFromDB();

                    for (Button btnd : drawer) {
                        showOnDrawerButton(btnd, parents, categories, subcategories);
                    }
                    if (auxiliary.size() < maxAuxiliarySize) {
                        auxiliary.add("");
                    } else {
                        buttonsClicked.remove(buttonsClicked.size() - 1);
                    }
                }
            });
        }

    }


    @FXML
   public void goBack() {
        ArrayList<Button> menu = new ArrayList<>();
        {
            menu.add(btnMenu1);
            menu.add(btnMenu2);
            menu.add(btnMenu3);
            menu.add(btnMenu4);
            menu.add(btnMenu5);
            menu.add(btnMenu6);
            menu.add(btnMenu7);
            menu.add(btnMenu8);
            menu.add(btnMenu9);
        }


        if (auxiliary.size() == 2) {
            if (buttonsClicked.size() > 0) {
                buttonsClicked.remove(buttonsClicked.size() - 1);
            }
            auxiliary.clear();

            parents = getParents();
            for (Button btnd : menu) {

                showOnDrawerButton(btnd, parents, categories, subcategories);
            }
            auxiliary.add("");

        }
        if (auxiliary.size() == 3) {

            if (buttonsClicked.size() > 0) {
                buttonsClicked.remove(buttonsClicked.size() - 1);
            }

            auxiliary.remove(auxiliary.size() - 1);
            auxiliary.remove(auxiliary.size() - 1);

            categories = getCategories(buttonsClicked.get(0));
            for (Button btnd : menu) {

                showOnDrawerButton(btnd, parents, categories, subcategories);
            }
            auxiliary.add("");
        }

    }

    @FXML
    void deleteSelectedPart(ActionEvent event) {
        int index = tableParts.getSelectionModel().getFocusedIndex();

        if(tableParts.getSelectionModel().getFocusedIndex() >= 0) {
            String DeleteQuery = null;
            PreparedStatement ps = null;
            Connection con = getConnection();

            try {
                DeleteQuery = "DELETE FROM parts WHERE id ='" + dataId.get(index) + "'";
                ps = con.prepareStatement(DeleteQuery);
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                DeleteQuery = "DELETE FROM tree WHERE id ='" + dataId.get(index) + "'";
                ps = con.prepareStatement(DeleteQuery);
                ps.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "Choose part to delete");
        }

    }

    public void crossVisibility() {
        ArrayList<Button> menu = new ArrayList<>();
        {
            menu.add(btnMenu1);
            menu.add(btnMenu2);
            menu.add(btnMenu3);
            menu.add(btnMenu4);
            menu.add(btnMenu5);
            menu.add(btnMenu6);
            menu.add(btnMenu7);
            menu.add(btnMenu8);
            menu.add(btnMenu9);
        }

        ArrayList<ImageView> crossImages = new ArrayList<>();
        {
            crossImages.add(imgviewDel1);
            crossImages.add(imgviewDel2);
            crossImages.add(imgviewDel3);
            crossImages.add(imgviewDel4);
            crossImages.add(imgviewDel5);
            crossImages.add(imgviewDel6);
            crossImages.add(imgviewDel7);
            crossImages.add(imgviewDel8);
            crossImages.add(imgviewDel9);
        }
        for(int i = 0; i < menu.size(); i++ ){
            if(menu.get(i).isVisible()) {
                crossImages.get(i).setVisible(true);
            }else{
                crossImages.get(i).setVisible(false);
            }


    }
    }

   public void crossImageAction(Button btn){
       ArrayList<Button> menu = new ArrayList<>();
       {
           menu.add(btnMenu1);
           menu.add(btnMenu2);
           menu.add(btnMenu3);
           menu.add(btnMenu4);
           menu.add(btnMenu5);
           menu.add(btnMenu6);
           menu.add(btnMenu7);
           menu.add(btnMenu8);
           menu.add(btnMenu9);
       }

       String DeleteQuery = null;
       PreparedStatement ps = null;
       Connection con = getConnection();

       if (buttonsClicked.size() >= 2) {
           auxiliary.remove(auxiliary.size()-1);
           try {
               DeleteQuery = "DELETE FROM tree WHERE parent ='" + buttonsClicked.get(0) + "'AND category ='" + buttonsClicked.get(1) + "'AND subcategory ='" +
                       btn.getText()  + "'";
               ps = con.prepareStatement(DeleteQuery);
               ps.executeUpdate();
           } catch (SQLException ex) {
               Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
           }
           subcategories = getSubcategories(buttonsClicked.get(0), buttonsClicked.get(1));

           for (Button btnd : menu) {
               showOnDrawerButton(btnd, parents, categories, subcategories);
           }
           auxiliary.add("");
       }

       if (buttonsClicked.size() == 1) {
           auxiliary.remove(auxiliary.size()-1);
           try {
               DeleteQuery = "DELETE FROM tree WHERE parent='" + buttonsClicked.get(0) +"'AND category ='" + btn.getText() + "'";
               ps = con.prepareStatement(DeleteQuery);
               ps.executeUpdate();
           } catch (SQLException ex) {
               Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
           }
           categories = getCategories(buttonsClicked.get(0));

           for (Button btnd : menu) {
               showOnDrawerButton(btnd, parents, categories, subcategories);
           }
           auxiliary.add("");
       }

       if (buttonsClicked.size() == 0) {
           auxiliary.remove(auxiliary.size()-1);
           try {
               DeleteQuery = "DELETE FROM tree WHERE parent='" + btn.getText() + "'";
               ps = con.prepareStatement(DeleteQuery);
               ps.executeUpdate();
           } catch (SQLException ex) {
               Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
           }
           parents = getParents();

           for (Button btnd : menu) {

               showOnDrawerButton(btnd, parents, categories, subcategories);
           }
           auxiliary.add("");
       }


   }


}

















