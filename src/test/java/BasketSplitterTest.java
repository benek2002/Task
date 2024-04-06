import org.example.BasketSplitter;
import org.json.simple.parser.ParseException;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static junit.framework.Assert.*;

public class BasketSplitterTest {
    @Test
    public void testMakeReverseMap() throws IOException, ParseException {

        //given
        String pathToConfigFile = System.getProperty("configFile");
        assertNotNull("No path to the configuration file", pathToConfigFile);
        BasketSplitter basketSplitter = new BasketSplitter(pathToConfigFile);
        List<String> items = List.of("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White");
        //when
        Map<String, List<String>> reversedMap = basketSplitter.makeReverseMap(items);
        //then
        assertEquals(7,reversedMap.size());
        assertTrue(reversedMap.containsKey("Next day shipping"));
        assertTrue(reversedMap.containsKey("Courier"));
        assertTrue(reversedMap.containsKey("Same day delivery"));
        assertEquals(2, reversedMap.get("Next day shipping").size());
        assertEquals(3, reversedMap.get("Courier").size());
        assertEquals(1, reversedMap.get("Express Collection").size());
        assertFalse(reversedMap.containsKey("Pick-up point"));
    }

    @Test
    public void testSortReversedMapByItemsCountDesc() throws IOException, ParseException {

        //given

        String pathToConfigFile = System.getProperty("configFile");
        assertNotNull("No path to the configuration file", pathToConfigFile);
        BasketSplitter basketSplitter = new BasketSplitter(pathToConfigFile);
        Map<String, List<String>> reverseMap = new HashMap<>();

        reverseMap.put("Next day shipping", Arrays.asList("Cocoa Butter", "Table Cloth 54x72 White"));
        reverseMap.put("Mailbox delivery", Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White"));
        reverseMap.put("In-store pick-up", Collections.singletonList("Cocoa Butter"));
        reverseMap.put("Parcel locker", Arrays.asList("Cocoa Butter", "Table Cloth 54x72 White"));
        reverseMap.put("Courier", Arrays.asList("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White"));
        reverseMap.put("Same day delivery", Collections.singletonList("Cocoa Butter"));
        reverseMap.put("Express Collection", Collections.singletonList("Tart - Raisin And Pecan"));

        //when

        Map<String, List<String>> sortedReversedMap = basketSplitter.sortReversedMapByItemsCountDesc(reverseMap);

        //then

        assertEquals(7,sortedReversedMap.size());
        assertTrue(sortedReversedMap.containsKey("Next day shipping"));
        assertTrue(sortedReversedMap.containsKey("Courier"));
        assertTrue(sortedReversedMap.containsKey("Same day delivery"));
        assertEquals(2, sortedReversedMap.get("Next day shipping").size());
        assertEquals(3, sortedReversedMap.get("Courier").size());
        assertEquals(1, sortedReversedMap.get("Express Collection").size());
        assertFalse(sortedReversedMap.containsKey("Pick-up point"));
        String firstKey = sortedReversedMap.keySet().iterator().next();
        assertEquals("Mailbox delivery", firstKey);
    }

    @Test
    public void testSplit() throws IOException, ParseException {

        //given
        String pathToConfigFile = System.getProperty("configFile");
        assertNotNull("No path to the configuration file", pathToConfigFile);
        BasketSplitter basketSplitter = new BasketSplitter(pathToConfigFile);
        List<String> items = List.of("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White");

        //when

        Map<String, List<String>> result = basketSplitter.split(items);

        //then

        assertEquals(1, result.size());
        assertEquals(3, result.get("Mailbox delivery").size());
    }
}
