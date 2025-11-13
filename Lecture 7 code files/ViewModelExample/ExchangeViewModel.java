package org.me.gcu.basicviewmodel;

import androidx.lifecycle.ViewModel;

/**
 * The ViewModel class survives the life cycle of activities/fragments it is used by.
 * Data created at runtime is lost when activities die (screen rotation or Android killing the app).
 * However, data stored here survive this. It is only lost if user closes the app.
 * <p>
 * It should implement the data logic here (if simple), or use a separate data processing class.
 * The controlling activity/fragment interacts with the widgets and exchanges data with the ViewModel
 * with the provided get/set methods.
 */
public class ExchangeViewModel extends ViewModel {
    private String amount = ""; //input data
    private static final float ratio = 1.14f; //for data processing
    private float result =0.0f; // results, this we want to preserve screen rotation

    /**
     * This sends data into the ViewModel.
     * It will be processed using whatever data logic is used (that could be done in a separate class).
     * Updates the results in private members, which survives screen rotation.
     * @param amount    String - input data to the model
     */
    public void setAmount(String amount) {
        this.amount = amount;
        result = Float.parseFloat(this.amount) * ratio;
    }

    /**
     * Outputs the results from the model.
     * This is used by the UI controller to get data, and show it on widgets.
     * The data members inside the ViewModel are preserved on screen rotations.
     * @return  Our resulting data (money exchange result in this case).
     */
    public String getResult() {
        return String.valueOf(result);
    }
}
