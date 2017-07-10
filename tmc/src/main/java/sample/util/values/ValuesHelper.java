package sample.util.values;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.google.common.reflect.ClassPath;

@Component("vh")
@Lazy
public class ValuesHelper {
	@Value("${app.values}")
	private static String values;

    private final Map<String, String> valuesObjList;

    private ValuesHelper() throws IOException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        // valuesObjList = ClassPath.from(loader).getTopLevelClassesRecursive(values)
        valuesObjList = ClassPath.from(loader).getTopLevelClassesRecursive("sample.customer.util.values")
                .stream()
                .filter(classInfo -> {
                    try {
                        Class<?> clazz = Class.forName(classInfo.getName());
                        return !clazz.equals(Values.class) && Values.class.isAssignableFrom(clazz);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toMap(ClassPath.ClassInfo::getSimpleName, ClassPath.ClassInfo::getName));
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T> & Values> String getValue(String classSimpleName, String valueName)
            throws ClassNotFoundException {
        Class<T> enumType = (Class<T>) Class.forName(this.valuesObjList.get(classSimpleName));
        T val = Enum.valueOf(enumType, valueName);
        return val.getValue();
    }

    public <T extends Enum<T> & Values> String getValue(Class<T> enumType, String valueName) {
        T val = Enum.valueOf(enumType, valueName);
        return val.getValue();
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T> & Values> String getText(String classSimpleName, String value)
            throws ClassNotFoundException {
        Class<T> enumType = (Class<T>) Class.forName(this.valuesObjList.get(classSimpleName));
        String result = "";
        for (T val : enumType.getEnumConstants()) {
            if (val.getValue().equals(value)) {
                result = val.getText();
                break;
            }
        }
        return result;
    }

    public <T extends Enum<T> & Values> String getText(Class<T> enumType, String value) {
        String result = "";
        for (T val : enumType.getEnumConstants()) {
            if (val.getValue().equals(value)) {
                result = val.getText();
                break;
            }
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T> & Values> T[] values(String classSimpleName)
            throws ClassNotFoundException {
        Class<T> enumType = (Class<T>) Class.forName(this.valuesObjList.get(classSimpleName));
        return enumType.getEnumConstants();
    }

}