package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.group.Group;
import seedu.address.model.group.GroupContainsKeywordsPredicate;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;
import seedu.address.model.tag.Tags;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PersonBuilder;

public class ModelManagerTest {

    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void tagExists_correctTag_returnsTrue() {
        Person person = new PersonBuilder().withTags("tag").build();
        Tag tag = new Tag("tag");
        Set<Tag> tagSet = Set.of(tag);
        Tags tags = new Tags(tagSet);
        Boolean actualBool = modelManager.tagExists(person, tags);
        assertEquals(true, actualBool);
    }

    @Test
    public void tagExists_wrongTag_returnsFalse() {
        Person person = new PersonBuilder().withTags("tag").build();
        Tag tag = new Tag("taggg");
        Set<Tag> tagSet = Set.of(tag);
        Tags tags = new Tags(tagSet);
        Boolean actualBool = modelManager.tagExists(person, tags);
        assertEquals(false, actualBool);
    }

    @Test
    public void addTagTest() {
        Person expectedPerson = new PersonBuilder().withTags("tag").build();
        Person actualPerson = new PersonBuilder().build();
        modelManager.addPerson(actualPerson);
        Tag tag = new Tag("tag");
        Set<Tag> setOfTags = Set.of(tag);
        Tags tags = new Tags(setOfTags);
        modelManager.addTag(actualPerson, tags);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void deleteTagTest() {
        Person actualPerson = new PersonBuilder().withTags("tag").build();
        Person expectedPerson = new PersonBuilder().build();
        modelManager.addPerson(actualPerson);
        Tag tag = new Tag("tag");
        Set<Tag> tagSet = Set.of(tag);
        Tags tags = new Tags(tagSet);
        modelManager.deleteTag(actualPerson, tags);
        assertEquals(expectedPerson, actualPerson);
    }

    @Test
    public void addGroup_validGroup_addsGroup() {
        Group group = new Group("group A", List.of(new PersonBuilder().build()));
        modelManager.addGroup(group);
        assertTrue(modelManager.hasGroupName(group));
    }

    @Test
    public void hasGroupName_nullGroup_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasGroupName(null));
    }

    @Test
    public void hasGroupName_groupNotInModel_returnsFalse() {
        Group group = new Group("group A", List.of(new PersonBuilder().build()));
        assertFalse(modelManager.hasGroupName(group));
    }

    @Test
    public void hasGroupName_groupInModel_returnsTrue() {
        Group group = new Group("group A", List.of(new PersonBuilder().build()));
        modelManager.addGroup(group);
        assertTrue(modelManager.hasGroupName(group));
    }

    @Test
    public void updateFilteredGroupList_nullPredicate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.updateFilteredGroupList(null));
    }

    @Test
    public void addToGroupList_returnsMatchingGroups() {
        Group groupA = new Group("groupA", List.of(new PersonBuilder().build()));
        Group groupB = new Group("groupB", List.of(new PersonBuilder().build()));
        Group groupC = new Group("groupC", List.of(new PersonBuilder().build()));
        modelManager.addGroup(groupA);
        modelManager.addGroup(groupB);
        modelManager.addGroup(groupC);

        assertTrue(modelManager.hasGroupName(groupA));
        assertTrue(modelManager.hasGroupName(groupB));
        assertTrue(modelManager.hasGroupName(groupC));
    }
    @Test
    public void updateFilteredGroupList_validPredicate_returnsMatchingGroups() {
        Group groupA = new Group("groupA", List.of(new PersonBuilder().build()));
        Group groupB = new Group("groupB", List.of(new PersonBuilder().build()));
        Group groupC = new Group("groupC", List.of(new PersonBuilder().build()));
        modelManager.addGroup(groupA);
        modelManager.addGroup(groupB);
        modelManager.addGroup(groupC);

        GroupContainsKeywordsPredicate predicate = new GroupContainsKeywordsPredicate(List.of("GroupA"));
        modelManager.updateFilteredGroupList(predicate);

        assertTrue(modelManager.getFilteredGroupList().contains(groupA));
        assertFalse(modelManager.getFilteredGroupList().contains(groupB));
        assertFalse(modelManager.getFilteredGroupList().contains(groupC));
    }



    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void getFilteredGroupList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredGroupList().remove(0));
    }


    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}
