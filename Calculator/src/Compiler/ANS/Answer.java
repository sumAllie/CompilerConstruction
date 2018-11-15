package Compiler.ANS;

import Compiler.PAR.Parser;
import Compiler.PAR.Node;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

public class Answer {


    public static String AOutput = "";
    static List<Node> nodeList = new ArrayList<>();


    private static float calc(Node node) {
        float val = -2222222;
        float val1, val2;

        if (null == node) {
            AOutput += "<Error>calc: syntax error.\n";
            return -1;
        }

        switch (node.getToken().getSymIndex()) {
            /*数字属性节点直接返回值*/
            case 0:
                val = Float.valueOf(node.getToken().getToken());
                break;
            case 1:
                val = Float.valueOf(node.getToken().getToken());
                break;

		/*操作符属性节点值需要先计算两个操作数的值，
			再根据操作符来计算最后的结果*/
            case 2:
                val1 = calc(node.getChildNode1());
                val2 = calc(node.getChildNode2());
                val = val1 + val2;
                break;
            case 3:
                val1 = calc(node.getChildNode1());
                val2 = calc(node.getChildNode2());
                val = val1 - val2;
                break;
            case 4:
                val1 = calc(node.getChildNode1());
                val2 = calc(node.getChildNode2());
                val = val1 * val2;
                break;
            case 5:
                val1 = calc(node.getChildNode1());
                val2 = calc(node.getChildNode2());
                val = val1 / val2;
                break;
            default:
                AOutput += "<Error>cal: Unknown operation.\n";
                break;
        }
        return val;
    }

    public static void AStart(BufferedReader br){
        nodeList = null;
        nodeList = Parser.PStart(br);

        int i = 0;
        AOutput = "";
        AOutput = AOutput + "----语义结果----\n";

        for (Node node: nodeList) {
            AOutput =  AOutput +  "第" + (++i) + "行算数式分析结果：" +  "\n";
            if (node == null){
                AOutput =  AOutput + "Syntax Error!" +  "\n";
            }
            else if (node.getNtIndex() == 3){
                AOutput = AOutput + "Syntax error" +  "\n";
            }
            else {
                float ans = calc(node);
                int a = (new Float(ans)).intValue();
                if(a == ans){
                    AOutput += a + "\n";
                }
                else {
                    AOutput += ans + "\n";
                }
            }

        }
    }

}
