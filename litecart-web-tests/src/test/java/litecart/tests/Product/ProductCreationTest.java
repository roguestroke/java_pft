package litecart.tests.Product;

import com.thoughtworks.xstream.XStream;
import litecart.model.ProductData;
import litecart.tests.TestBase;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class ProductCreationTest extends TestBase {

    @DataProvider
    public Iterator<Object[]> validProduct() throws IOException {
//      List<Object[]> list = new ArrayList<>();
        BufferedReader reader = new BufferedReader
                (new FileReader(new File("src/test/resources/products.xml")));
        String xml = "";
        String line = reader.readLine();
        while (line != null) {
            xml += line;
//            Для формата csv
//            String[] split = line.split(";");
//            list.add(new Object[] {new ProductData().withName(split[0]).withShortDescription(split[1])
//                    .withDescription(split[2]).withTechnicalData(split[3])});
            line = reader.readLine();
        }
        XStream xStream = new XStream();
        xStream.processAnnotations(ProductData.class);
        List<ProductData> products = (List<ProductData>)xStream.fromXML(xml);
        return products.stream().map((g) -> new Object[] {g}).collect(Collectors.toList()).iterator();
//        return list.iterator();
    }

    @Test (dataProvider = "validProduct")
    public void testProductCreation(ProductData product) {
        app.checkAdminMainPageIsTrue();
        app.goTo().Catalog();
        Set<ProductData> before = app.product().all();
        app.product().create(product);
        Set<ProductData> after = app.product().all();
        Assert.assertEquals(after.size(), before.size() + 1);

        product.withId(after.stream().max(Comparator.comparingInt(ProductData::getId)).get().getId());
        before.add(product);
        Assert.assertEquals(before, after);

//        int max =0;
//        for (ProductData g: after) {
//            if (g.getId() > max) {
//                max = g.getId();
//            }
//        }
//        group.setId(max);

// Преобразовал цикл, вводим анонимную функцию (лямбда выражение) ->
// Новый список превращаем в поток.
// У потока есть метод max, в который позволяет вычислять макс. элемент.
// Передаем компаратор (сравниватель) в качестве параметра. Получаем макс. элемент и его Id

//        before.add(group);
//        Comparator<? super ProductData> byId = Comparator.comparingInt(ProductData::getId);
//        before.sort(byId);
//        after.sort(byId);
//        Assert.assertEquals(before, after);

    }
}

