package sample;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;


public class Part {
    private final StringProperty name;
    private ImageView image;
    private final IntegerProperty lego_used;
    private final IntegerProperty create_used;
    private final IntegerProperty parts_to_use;
    public Part(String name, ImageView img, int lego_used, int create_used, int parts_to_use) {
        this.name = new SimpleStringProperty(name);
        this.image = img;
        this.lego_used = new SimpleIntegerProperty(lego_used);
        this.create_used = new SimpleIntegerProperty(create_used);
        this.parts_to_use = new SimpleIntegerProperty(parts_to_use);



    }

    public String getName() {
        return name.get();
    }
    public ImageView getImage() {
        return image;
    }
    public int getLego_used() {
        return lego_used.get();
    }
    public int getCreate_used() {
        return create_used.get();
    }
    public int getParts_to_use() {
        return parts_to_use.get();
    }



    public void setName(String value) { name.set(value); }

    public  void setImage(ImageView value) {
        image = value;
        }

    public void setLego_used(int value) {
        lego_used.set(value);
    }

    public void setCreate_used(int value) {
        create_used.set(value);
    }
    public void setParts_to_use(int value) {
        parts_to_use.set(value);
    }


    public StringProperty nameProperty(){
        return name;
    }
    public IntegerProperty legoProperty(){
        return lego_used;
    }
    public IntegerProperty createProperty() {
        return create_used;
    }
    public IntegerProperty partsProperty() {
        return parts_to_use;
    }

}



