package App;
public class firewall {
    public String input;
    firewall(String input) { // When you get your first lesson in OOP
        this.input = input;
        }
    public String run() {
        System.out.println("[FIREWALL ALERT] The firewall is working..");
        if(input.length()>65538) {
            input = "REJECTED"; // What the fawk :) ?
            System.out.println("[FIREWALL ALERT] A7A! A +64KB Packet Detected.");
            return input;
            }
        input = input.replaceAll("[#$%^&*~`{}\\\"';=+<>|-]", ""); //Filtering
        System.out.println("[FIREWALL ALERT] Passed All Checkpoints, Continuing..");
        return input; // Security Stonks
        }
}