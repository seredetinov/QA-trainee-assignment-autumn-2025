package resources.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

// Чтобы можно было проводить негативные проверки, связанные с некорректным типом, все поля DTO-классов имеют тип Object
// Аннотация ActualType служит для указания фактического, валидного типа поля
@Target(ElementType.TYPE_USE)
public @interface ActualType {
    Class <?> value();
}
