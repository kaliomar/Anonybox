package App;
public class firewall {
    public String input;
    firewall(String input) {
        this.input = input;
        }
    public String run() {
        if(input.length()>65538) {
            input = "REJECTED";
            return input;
            }
        input = input.replaceAll("[#$%^&*~`{}\\\"';=+<>|-]", ""); //Filtering
        return input;
        }
}