package eu.faircode.xlua.random;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import eu.faircode.xlua.random.randomizers.Random3DigitNumber;
import eu.faircode.xlua.random.randomizers.RandomAdID;
import eu.faircode.xlua.random.randomizers.RandomAlphaNumeric;
import eu.faircode.xlua.random.randomizers.RandomAndroidID;
import eu.faircode.xlua.random.randomizers.RandomBluetoothState;
import eu.faircode.xlua.random.randomizers.RandomBoolean;
import eu.faircode.xlua.random.randomizers.RandomCarrierName;
import eu.faircode.xlua.random.randomizers.RandomDNS;
import eu.faircode.xlua.random.randomizers.RandomDRM;
import eu.faircode.xlua.random.randomizers.RandomDataState;
import eu.faircode.xlua.random.randomizers.RandomGSF;
import eu.faircode.xlua.random.randomizers.RandomGameID;
import eu.faircode.xlua.random.randomizers.RandomHostName;
import eu.faircode.xlua.random.randomizers.RandomICCID;
import eu.faircode.xlua.random.randomizers.RandomIMEI;
import eu.faircode.xlua.random.randomizers.RandomMAC;
import eu.faircode.xlua.random.randomizers.RandomMEID;
import eu.faircode.xlua.random.randomizers.RandomMSIN;
import eu.faircode.xlua.random.randomizers.RandomMemory;
import eu.faircode.xlua.random.randomizers.RandomNetAddress;
import eu.faircode.xlua.random.randomizers.RandomNetD;
import eu.faircode.xlua.random.randomizers.RandomNetworkType;
import eu.faircode.xlua.random.randomizers.RandomPhoneNumber;
import eu.faircode.xlua.random.randomizers.RandomPhoneType;
import eu.faircode.xlua.random.randomizers.RandomSIMCount;
import eu.faircode.xlua.random.randomizers.RandomSIMID;
import eu.faircode.xlua.random.randomizers.RandomSIMState;
import eu.faircode.xlua.random.randomizers.RandomSIMType;
import eu.faircode.xlua.random.randomizers.RandomSSID;
import eu.faircode.xlua.random.randomizers.RandomSerial;
import eu.faircode.xlua.random.randomizers.RandomSimSerial;
import eu.faircode.xlua.random.randomizers.RandomSubUsage;
import eu.faircode.xlua.random.randomizers.RandomSubscriberID;
import eu.faircode.xlua.random.randomizers.RandomUserAgentManager;
import eu.faircode.xlua.random.randomizers.RandomVoiceMailID;

public class GlobalRandoms {
    private static final Object lock = new Object();
    public static Map<String, IRandomizer> randomizers = new Hashtable<>();

    public static void putRandomizer(IRandomizer randomizer) { synchronized (lock) { randomizers.put(randomizer.getSettingName(), randomizer); } }
    public static List<IRandomizer> getRandomizers() {
        if(randomizers.isEmpty()) initRandomizers();
        synchronized (lock) {
            List<IRandomizer> localCopy = new ArrayList<>(randomizers.values());
            localCopy.add(new RandomUserAgentManager());
            return localCopy;
        }
    }

    public static void initRandomizers() {
        putRandomizer(new RandomAndroidID());
        putRandomizer(new RandomDRM());
        putRandomizer(new RandomGSF());
        putRandomizer(new RandomIMEI());
        putRandomizer(new RandomMAC());
        putRandomizer(new RandomMEID());
        putRandomizer(new RandomAdID());
        putRandomizer(new RandomSimSerial());
        putRandomizer(new RandomSubscriberID());
        putRandomizer(new RandomSSID());
        putRandomizer(new RandomMemory());
        putRandomizer(new RandomDNS());
        putRandomizer(new RandomNetAddress());
        putRandomizer(new RandomHostName());
        putRandomizer(new RandomHostName());
        putRandomizer(new RandomSerial());
        putRandomizer(new RandomCarrierName());
        putRandomizer(new RandomMSIN());
        putRandomizer(new RandomSIMID());
        putRandomizer(new RandomSIMType());
        putRandomizer(new RandomSubUsage());
        putRandomizer(new RandomSIMState());
        putRandomizer(new RandomSIMCount());
        putRandomizer(new RandomAlphaNumeric());
        putRandomizer(new RandomPhoneType());
        putRandomizer(new Random3DigitNumber());
        putRandomizer(new RandomNetworkType());
        putRandomizer(new RandomDataState());
        putRandomizer(new RandomBluetoothState());
        putRandomizer(new RandomNetD());
        putRandomizer(new RandomICCID());
        putRandomizer(new RandomPhoneNumber());
        putRandomizer(new RandomVoiceMailID());
        putRandomizer(new RandomGameID());
        putRandomizer(new RandomBoolean());
    }
}
