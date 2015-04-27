package util.lambdaplus.examples;
/*
public class ExampleA {
    private static final Long ROOT_PARENT_ID = 0l;
    private SecureItemRepository aclSecuredItemRepository;

    public static class SecureItemRepository {

        public void hasMovePermission(Long newParentId, Item itemToMove, User requestor) throws ExceptionC {
        }
    }

    public static class ExceptionA extends Exception {
        public ExceptionA(String s) {
            super(s);
        }
    }

    public static class ExceptionB extends Exception {
        public ExceptionB(String s) {
            super(s);
        }
    }

    public static class ExceptionC extends Exception {
        public ExceptionC(String s) {
            super(s);
        }
    }

    public static class Item {

        private Object name;
        private Long id;
        private Long parentId;

        public Object getName() {
            return name;
        }

        public Object getItemType() {
            return itemType;
        }

        public Long getId() {
            return id;
        }

        public Long getParentId() {
            return parentId;
        }

        public User getOwner() {
            return owner;
        }

        public boolean isOwner(User user) {
            return false;
        }
    }

//    public static class User {
//
//    }

    public static class User {

    }

    public static class ItemRepository {

        public Item loadFromPK(Long newParentId) throws ExceptionC {
            return null;
        }

        public boolean isNameUniqueWithinParent(Long newParentId, Object name, User owner) {
            return false;
        }
    }

    ItemRepository itemRepository;

    static enum ItemType {
        OBJECT, COLLECTION
    }

    public boolean validateMoveRequest(Long newParentId, Item itemToMove, User requestor)
            throws ExceptionA, ExceptionC, ExceptionB {
        if (newParentId == null || itemToMove.getParentId().equals(newParentId)) {
            return false;
        }
        if (itemToMove.getId().equals(newParentId)) {
            throw new ExceptionA("Could not move '%s' to be a parent of itself");
        }

        if (!ROOT_PARENT_ID.equals(newParentId)) {
            Item parentCollection = itemRepository.loadFromPK(newParentId);
            if (parentCollection == null || ItemType.OBJECT.equals(parentCollection.getItemType())) {
                throw new ExceptionA("Invalid parent collection '%s'");
            }
            if (!itemToMove.getOwner().equals(parentCollection.getOwner())) {
                throw new ExceptionA("Invalid parent collection '%s'");
            }
            hierarchyCheck(newParentId, itemToMove, parentCollection);
            commonAncestryCheck(newParentId, itemToMove, requestor, parentCollection);
        } else if (!itemToMove.isOwner(requestor)) {
            throw new ExceptionA("Invalid parent collection '%s'");
        }
        aclSecuredItemRepository.hasMovePermission(newParentId, itemToMove, requestor);
        if (!itemRepository.isNameUniqueWithinParent(newParentId, itemToMove.getName(), itemToMove.getOwner())) {
            throw new ExceptionB("'%s' is not a unique name for items in the specified destination");
        }
        return true;
    }

//    public boolean validateMoveRequest2(Long newParentId, Item itemToMove, User requestor)
//            throws ExceptionA, ExceptionC, ExceptionB {
//
//        EitherFunction<Tuple3<Long, Item, User>, Exception, Boolean> validate
//                =
//
//        if (newParentId == null || itemToMove.getParentId().equals(newParentId)) {
//            return false;
//        }
//        if (itemToMove.getId().equals(newParentId)) {
//            throw new ExceptionA("Could not move '%s' to be a parent of itself");
//        }
//
//        if (!ROOT_PARENT_ID.equals(newParentId)) {
//            Item parentCollection = itemRepository.loadFromPK(newParentId);
//            if (parentCollection == null || ItemType.OBJECT.equals(parentCollection.getItemType())) {
//                throw new ExceptionA("Invalid parent collection '%s'");
//            }
//            if (!itemToMove.getOwner().equals(parentCollection.getOwner())) {
//                throw new ExceptionA("Invalid parent collection '%s'");
//            }
//            hierarchyCheck(newParentId, itemToMove, parentCollection);
//            commonAncestryCheck(newParentId, itemToMove, requestor, parentCollection);
//        } else if (!itemToMove.isOwner(requestor)) {
//            throw new ExceptionA("Invalid parent collection '%s'");
//        }
//        aclSecuredItemRepository.hasMovePermission(newParentId, itemToMove, requestor);
//        if (!itemRepository.isNameUniqueWithinParent(newParentId, itemToMove.getName(), itemToMove.getOwner())) {
//            throw new ExceptionB("'%s' is not a unique name for items in the specified destination");
//        }
//        return true;
//    }


    private void commonAncestryCheck(Long newParentId, Item itemToMove, User requestor,
                                     Item parentCollection) throws ExceptionC {
    }

    private void hierarchyCheck(Long newParentId, Item itemToMove, Item parentCollection) throws ExceptionC{
    }
}
*/