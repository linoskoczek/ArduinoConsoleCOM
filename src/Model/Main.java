package Model;

import Model.Model;
import View.View;
import Controller.Controller;

public class Main {
    private static Model model;
    private static View view;
    private static Controller controller;

    public static void main(String... args) {
        model = new Model();
        View view = new View(model);
        Controller controller = new Controller(model, view);
    }

    public static void createNewModel() {
        view.updateModel(model);
        controller.updateModel(model);
    }
}
