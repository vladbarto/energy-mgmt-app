package ro.tucn.energy_mgmt_user.mapper;

import java.util.List;

public interface GenericMapper<Entity, RequestDTO, ResponseDTO> {

    /**
     * Method that receives data (one object) from DB and prepares it as a response
     * @param entity - response from DB
     * @return data in form of a response dto object
     */
    ResponseDTO entityToResponseDTO(Entity entity);

    /**
     * Method that receives data (more objects) from DB and prepares it as a response
     * @param entityList - a list of (all) objects from DB
     * @return list of (all) data in form of a response dto
     */
    List<ResponseDTO> entityListToResponseDTOList(List<Entity> entityList);

    /**
     * Method that gets data from user, and it is asked to update the DB
     * @param requestDTO - an object from user (client) to be stored in DB
     * @return the object suitable for DB update
     */
    Entity requestDTOToEntity(RequestDTO requestDTO);
}