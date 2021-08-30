
@Slf4j
public class BaseConverter<T extends BaseEntity,S,V> {


     public static  <T extends BaseEntity, S>  T toEntity(S request, Class<T> tClass)
             throws InstantiationException, IllegalAccessException {
         log.info("Converter: Request is {}",request);

         Class sClass = request.getClass();

         T entity = tClass.newInstance();

         Stream<Field> tFields = Arrays.stream(tClass.getDeclaredFields());
         log.info("Converter: Entity Fields = {} ", tClass.getFields().length);
         List<Field> commonFieldsBetweenEntityAndRequest = new ArrayList<>();

         tFields.forEach(
                    field -> {
                        if (Arrays.stream(sClass.getDeclaredFields()).anyMatch((f) -> f.getName().equals(field.getName()))) {
                            commonFieldsBetweenEntityAndRequest.add(field);
                        }
                    }
                );

        commonFieldsBetweenEntityAndRequest.forEach(
                field -> {
                    try {
                      log.info("Field name is {}",field.getName());

                       Field fieldOfEntity = tClass.getDeclaredField(field.getName());
                       Field fieldOfRequest = sClass.getDeclaredField(field.getName());

                       fieldOfRequest.setAccessible(true);
                       fieldOfEntity.setAccessible(true);

                        log.info("Converter: Value of {} in request is {}",fieldOfRequest.getName()
                                ,fieldOfRequest.get(request));

                        if (fieldOfRequest.get(request) != null) {
                           fieldOfEntity.set(entity,fieldOfRequest.get(request));
                        }
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        );
        return entity;
    }

    static <T extends BaseEntity,V> V  entityToDto(T entity,Class<V> dtoClass) {
         Stream<Field> fieldsOfDto = Arrays.stream(dtoClass.getDeclaredFields());
         Stream<Field> fieldsOfEntity = Arrays.stream(entity.getClass().getDeclaredFields());
         return null;
    }

    Page<V> entityPageToDtoPage(T entity){ return null; }
}
