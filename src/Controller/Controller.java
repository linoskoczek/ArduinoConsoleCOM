package Controller;

import Model.Model;
import View.View;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        createActionListeners();
    }

    public void updateModel(Model model) {
        view.removeActionListeners();
        this.model = model;
        createActionListeners();
        view.restartModelListener();
    }

    private void createActionListeners() {
        view.addSendListener(actionEvent -> model.write(view.getAndClearUserInput()));
        view.addSendViaEnterListener(actionEvent -> model.write(view.getAndClearUserInput()));
    }
}
