package Model;

import Controller.Controller;
import View.View;

public class Main {
    private static Model model;
    private static View view;
    private static Controller controller;

    public static void main(String... args) {
        model = new Model();
        view = new View(model);
        controller = new Controller(model, view);
    }

    public static void createNewModel() {
        model = new Model();
        view.updateModel(model);
        controller.updateModel(model);
    }
}
