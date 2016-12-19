package org.hyperledger.fabric.example;

import com.google.protobuf.ByteString;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.sdk.shim.ChaincodeBase;
import org.hyperledger.fabric.sdk.shim.ChaincodeStub;

/**
 * Created by cadmin on 12/10/16.
 */
public class RainfallContract extends ChaincodeBase {
    private static Log log = LogFactory.getLog(RainfallContract.class);

    @Override
    public String run(ChaincodeStub stub, String function, String[] args) {

        log.info("Run with function and arg " + function +
                " - arg - " + StringUtils.join(args, '|'));
        String insuranceID, state;
        float insuredAmount;
        int rainThreshold;
        RainfallInsurance rf;
        try {
            switch (function) {
                case "init":
                    stub.putState("total_premium", "0");
                    break;
                case "new":
                    insuranceID = args[0];
                    state = args[1];
                    rainThreshold = Integer.parseInt(args[2]);
                    insuredAmount = Float.parseFloat(args[3]);
                    rf = new RainfallInsurance(insuranceID, state, rainThreshold, insuredAmount);
                    // TODO HW to update the total premium for every new contract
                    // stub.getState("total_premium");
                    log.info(rf);
                    stub.putRawState(insuranceID, ByteString.copyFrom(rf.toByteArr()));
                    break;
                case "execute":
                    insuranceID = args[0];
                    int recordedRain = Integer.parseInt(args[1]);
                    rf = RainfallInsurance.toPojo(stub.getRawState(insuranceID).toByteArray());
                    rf.execute(recordedRain);
                    log.info(rf);
                    stub.putRawState(insuranceID, ByteString.copyFrom(rf.toByteArr()));
                    break;
                default:
                    log.fatal("Invalid function passed " + function);
                    throw new RuntimeException("Unsupported method");
            }
        } catch (NumberFormatException nf) {
            log.error("Invalid arguments");
            throw new RuntimeException("NumberFormatException " + nf.getMessage());

        }
        return "";
    }

    @Override
    public String query(ChaincodeStub stub, String function, String[] args) {
        String insuranceID = args[0];
        return RainfallInsurance.toPojo(stub.getRawState(insuranceID).toByteArray()).toString();
        }

    @Override
    public String getChaincodeID() {
        return "RainfallContract";
    }

    public static void main(String[] args) {
        new RainfallContract().start(args);
    }
}
