package physics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Polynomial implements Func {
    public String name = null;
    
    public Map<List<Integer>, Double> coefficients;
    
    /**
     * @param n number of variables.
     */
    public Polynomial() {
        this.coefficients = new HashMap<List<Integer>, Double>();
    }
    
    public void set(double val, int...vars) {
        List<Integer> varList = toList(vars);
        coefficients.put(varList, val);
    }
    
    private List<Integer> toList(int[] vars) {
        List<Integer> varList = new ArrayList<Integer>();
        for (int i : vars) {
            varList.add(i);
        }
        varList.sort((x,y) -> Integer.compare(x, y));
        return Collections.unmodifiableList(varList);
    }
    
    public double eval(double[] variables) {
        double sum = 0;
        
        for (List<Integer> vars : coefficients.keySet()) {
            double term = coefficients.get(vars);
            for (Integer var : vars) {
                term *= variables[var];
            }
            sum += term;
        }
        
        return sum;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (List<Integer> vars : coefficients.keySet()) {
            double coeff = coefficients.get(vars);
            if (sb.length() > 0) {
                if (coeff >= 0) {
                    sb.append(" + ");
                } else {
                    sb.append(" - ");
                    coeff = -coeff;
                }
            }
            if (Math.abs(coeff - 1) > 0.01) {
                sb.append(String.format("%.1f", coeff));
            }
            
            int multiplicity = 1;
            for (int idx = 0; idx < vars.size()-1; idx++) {
                int cur = vars.get(idx);
                int next = vars.get(idx + 1);
                if (cur == next) {
                    multiplicity++;
                } else {
                    sb.append("(x"+cur+")");
                    if (multiplicity > 1) {
                        sb.append("^" + multiplicity);
                    }
                    multiplicity = 1;
                }
            }
            if (vars.size() > 0) {
                int last = vars.get(vars.size()-1);
                sb.append("(x"+last+")");
                if (multiplicity > 1) {
                    sb.append("^" + multiplicity);
                }
            }
        }
        
        if (sb.length() == 0) {
            sb.append("0");
        }

        return sb.toString();
    }
    
}
