package Controller;

import Model.Model;
import View.View;

public class Controller {
    private Model model;
    private View view;

    public Controller(Model model, View view) {
        this.model = model;
        this.view = view;

        Thread modelListener = new Thread(this::startModelListener);
        modelListener.start();

        view.addSendListener(actionEvent -> model.write(view.getAndClearUserInput()));
        view.addSendViaEnterListener(actionEvent -> model.write(view.getAndClearUserInput()));
    }

    private void startModelListener() {
        String message;
        while(model.isRunning) {
            message = model.getMessage();
            if(message == null) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException ignored) {}
            } else {
                view.gotMessageEvent(message);
            }
        }
    }
}
