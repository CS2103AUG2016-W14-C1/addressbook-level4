# Test Script

### Overview

This is a document which explains the steps to perform manual testing. Please note that this document is accurate as of 9 November 2016 due to the time-sensitive nature of commands (e.g. use of keyword today). If you are testing on another date, it is highly recommended to set the System time to 9 November 2016 to achieve the intended results described in this document. 

### Terminologies

An activity in Remindaroo can be either:
* Task: an activity without a specified time
* Deadline: an activity with a specified time
* Event: an activity with a specific start time and specific end time

An activity in Remindaroo has a Status of either:
* pending: activity to be done
* completed: activity which is done

The GUI of Remindaroo consists of:
* Today's Date Header: displays Remindaroo's icon and today's date
* Schedule Display Panel: displays list of deadlines and/or events, with its ID, Name, Start Date (if any), End Date, and Status sorted in chronological order
* Tasks Display Panel: displays list of tasks, with its ID, Name and Status
* Result Display Section: display feedback upon entering a command
* Command Box: a text field for user to input command which user wishes to execute on Remindaroo
* Status Bar: displays Last Updated date (if it is updated in this session) and current stored file location (default **data/remindaroo.xml**)

Activities in Schedule Display Panel and Tasks Display Panel are first sorted by Status (pending then completed), followed by chronological order of start/end date (if any), followed by time added.

We will refer to the above parts when we describe the steps for manual testing as well as for the expected results.

### Test cases

The test cases mentioned below should be done sequentially in order to get the correct results and behaviors.

### Load sample data

1. Ensure that you have Java Version 1.8.0_60 or later installed in your computer
2. Download the latest remindaroo.jar from the releases tab
3. Copy remindaroo.jar to the folder you wish to use as the home folder for Remindaroo, such as User/John/Documents.
4. Access [SampleData.xml](https://raw.githubusercontent.com/CS2103AUG2016-W14-C1/main/master/src/test/data/ManualTesting/SampleData.xml) in Remindaroo's Github repository. Copy the file contents to a text editor (e.g. Notepad) and save it in a file named `SampleData.xml`.
5. Move SampleData.xml to be in the same directory as that of remindaroo.jar
6. Double-click the file to start Remindaroo. The Graphical User Interface (GUI) should appear in a few seconds.
Status bar should display **Not updated yet in this session** and current stored file location (**data/remindaroo.xml**).
7. Enter `load SampleData.xml` in the Command Box.
8. Schedule Display Panel and Tasks Display Panel should be loaded with sample data. 
Result Display Section should display **Remindaroo has loaded data from SampleData.xml**. 
Status Bar should change to the default display.

---

### `add` command

### Normal paths
#### Add a task
1. Enter `add buy milk` in the Command Box.
2. Result Display Section should display **New activity added: buy milk**.<br>
Tasks Display Panel should add on with an activity **buy milk** with status **pending**.

#### Add a deadline
1. Enter `add assignment 3 on tmr 23:59` in the Command Box.
2. Result Display Section should display **New activity added: assignment 3**.<br>
Schedule Display Panel should add on with this activity and display its details.

#### Add an event
1. Enter `add CS2103T Exam from 26 Nov 1pm to 26 Nov 3pm` in the Command Box.
2. Result Display Section should display **New activity added: CS2103T Exam**.<br>
Schedule Display Panel should add on with this activity and display its details.

#### Add recurring deadline
1. Enter `add Milestone Iteration by fri 23:59 for 4 weeks` in the Command Box.
2. Result Display Section should display **New recurring activity added: Milestone Iteration**.<br>
Schedule Display Panel should add on with 4 activities (with same name but each activity is separated by a week apart) and display its details.

#### Add recurring event
1. Enter `add morning jog from 7am to 8am for 10 days` in the Command Box.
2. Result Display Section should display **New recurring activity added: morning jog**.<br>
Schedule Display Panel should add on with 10 activities (with same name but each activity is separated by a day apart) and display its details.

### Special paths
#### Keywords case-insensitive
1. Enter `ADD swimming lesson From thu 9am to thu 10am` in the Command Box.
2. Result Display Section should display **New activity added: swimming lesson**.<br>
Schedule Display Panel should add on with this activity and display its details.

#### Activity name contains keywords
1. Enter `add John on air "on" 13 nov 7pm` in the Command Box.
2. Result Display Section should display **New activity added: John on air**.<br>
Schedule Display Panel should add on with this activity and display its details.

### Error paths
#### Add nothing
1. Enter `add` in the Command Box.
2. Result Display Section should display **Invalid command format! ...** (usage of add command).<br>
Command Box should turn red.

#### Event end date earlier than start date
1. Enter `add invalid event from today to yesterday` in the Command Box.
2. Result Display Section should display **Event has already ended before it starts.**<br>
Command Box should turn red.

---

### `list` command
1. Enter `list` in the Command Box.
2. Result Display Section should display **Listed all activities**.<br>
Task Display Panel and Schedule Display Panel should display all activities in the current stored file location.

---

### `update` command

### Normal paths
#### Update a task
1. Enter `update 68 buy eggs` in the Command Box.
2. Result Display Section should display **Updated activity: buy eggs**.<br>
Tasks Display Panel should update activity 68 (**buy milk**) with new name.

#### Update a deadline name
1. Enter `update 13 written report` in the Command Box.
2. Result Display Section should display **Updated activity: written report**.<br>
Schedule Display Panel should update activity 13 (**assignment 3**) with new name.

#### Update a deadline name and date
1. Enter `update 13 written report II by fri 23:59` in the Command Box.
2. Result Display Section should display **Updated activity: written report II**.<br>
Schedule Display Panel should update activity 13 (**written report**) with new name and end date, which should be now at activity 17.

#### Update an event date
1. Enter `update 16 from 12 Nov 20:00 to 13 Nov 01:00` in the Command Box.
2. Result Display Section should display **Updated activity: Partying Partying Partying**.<br>
Schedule Display Panel should update activity 16 (**Partying Partying Partying**) with new start and end time, which should be now at activity 25.

#### Update an event name and date
1. Enter `update 25 Reservice from fri 23:59 to sun 16:00` in the Command Box.
2. Result Display Section should display **Updated activity: Reservice**.<br>
Schedule Display Panel should update activity 25 (**Partying Partying Partying**) with new name, start and end time, which should be now at activity 20.

### Special paths
#### Update a task to a deadline
1. Enter `update 63 by next tue 12pm` in the Command Box.
2. Result Display Section should display **Updated activity: update passport**.<br>
Activity 63 (**update passport**) should transfer from Task Display Panel to Schedule Display Panel with new time, which should be at activity 35.

#### Update a task to an event
1. Enter `update 65 from 14 nov 7pm to 14 nov 9pm` in the Command Box.
2. Result Display Section should display **Updated activity: go to gym**.<br>
Activity 65 (**go to gym**) should transfer from Task Display Panel to Schedule Display Panel with new time, which should be at activity 33.

#### Update a deadline to an event
1. Enter `update 30 from 13 nov 7pm to 13 nov 7.15pm` in the Command Box.
2. Result Display Section should display **Updated activity: John on air**.<br>
Schedule Display Panel should update activity 30 (**John on air**) with new start and end time.

#### Update an event to a deadline
1. Enter `update 21 on sun 10pm` in the Command Box.
2. Result Display Section should display **Updated activity: SOMETHING**.<br>
Schedule Display Panel should update activity 21 (**hangover**) with new end time and remove its start time, which should be at activity 30.

#### Update name contains keywords
1. Enter `update 61 welcome friend from Japan "from" 13 dec 7pm to 13 dec 9pm` in the Command Box.
2. Result Display Section should display **Updated activity: welcome friend from Japan**.<br>
Schedule Display Panel should update activity 61 (**welcome friend from Japan**) with new name start and end time.

### Error paths
#### Update invalid index
1. Enter `update 99 invalid` in the Command Box.
2. Result Display Section should display **The activity index provided is invalid**.<br>
Command Box should turn red.

#### Update nothing
1. Enter `update` in the Command Box.
2. Result Display Section should display **Invalid command format! ...** (usage of update command).<br>
Command Box should turn red.

#### Event end date earlier than start date
1. Enter `update 1 invalid event from today to yesterday` in the Command Box.
2. Result Display Section should display **Event has already ended before it starts.**<br>
Command Box should turn red.

---

### `delete` command

### Normal paths
#### Delete an activity
1. Enter `delete 26` in the Command Box.
2. Result Display Section should display **Deleted activity: morning jog**.<br>
Tasks Display Panel should remove activity 26 (**morning jog**).

#### Delete multiple activities
1. Enter `delete 23 24 21 22 64` in the Command Box.
2. Result Display Section should display **Deleted Activity / Activities: arrange meetup with Joe mugging grandma sleep visit grandma morning jog**.<br>
Tasks Display Panel and Schedule Display Panel should remove activity 64 (**arrange meetup with Joe**), 24 (**mugging**), 23 (**grandma sleep**), 22 (**visit grandma**), 21 (**morning jog**).

### Error paths
#### Delete invalid index
1. Enter `delete 99` in the Command Box.
2. Result Display Section should display **The activity index provided is invalid**.<br>
Command Box should turn red.

#### Delete negative index
1. Enter `delete -1` in the Command Box.
2. Result Display Section should display **Invalid command format! ...** (usage of delete command).<br>
Command Box should turn red.

---

### `undo` command

### Normal paths
#### Undo default
1. Enter `undo` in the Command Box.
2. Result Display Section should display **Reverted to previous state. (1 commands undone)**.<br>
The previous command should be undone.

#### Undo multiple times
1. Enter `undo 3` in the Command Box.
2. Result Display Section should display **Reverted to previous state. (3 commands undone)**.<br>
The previous 3 commands should be undone.

### Special paths
#### Undo too many times
1. Enter `undo 999` in the Command Box.
2. Result Display Section should display **Insufficient number of commands to perform undo operation.**

---

### `redo` command

### Normal paths
#### Redo default
1. Enter `redo` in the Command Box.
2. Result Display Section should display **Reverted to later state. (1 commands redone)**.<br>
The previous command should be redone.

#### Redo multiple times
1. Enter `redo 2` in the Command Box.
2. Result Display Section should display **Reverted to later state. (2 commands redone)**.<br>
The previous 2 commands should be redone.

### Special paths
#### Redo too many times
1. Enter `redo 999` in the Command Box.
2. Result Display Section should display **Insufficient number of commands to perform redo operation. You can redo maximum 1 times.**.

#### Nothing to redo
1. Enter `redo` in the Command Box.
2. **Reverted to later state. (1 commands redone)**.
3. Enter `redo` in the Command Box.
4. Result Display Section should display **Nothing left to redo. You are at the latest state.**.

---

### `search` command

### Normal paths
#### Search by a keyword
1. Enter `search "buy"` in the Command Box.
2. Result Display Section should display **2 activities listed!**<br>
Schedule Display Panel should show activities with the keyword "buy".

#### Search by multiple keywords
1. Enter `search 'CS2101 cs2103t'` in the Command Box.
2. Result Display Section should display **8 activities listed!**<br>
Schedule Display Panel should show activities with the keyword "CS2101" and/or "cs2103t" (case-insensitive).

#### Search by a date
1. Enter `search today` in the Command Box.
2. Result Display Section should display **4 activities listed!**<br>
Schedule Display Panel should show all activities which occur today.

#### Search by a range of two dates
1. Enter `search today to tomorrow` in the Command Box.
2. Result Display Section should display **9 activities listed!**<br>
Schedule Display Panel should show all activities with start/end date falls in the range from today to tomorrow.

#### Search by status
1. Enter `search pending` in the Command Box.
2. Result Display Section should display **60 activities listed!**<br>
Tasks Display Panel and Schedule Display Panel should show all pending activities.

### Special paths
#### Search which returns no result
1. Enter `search 'blablabla'` in the Command Box.
2. Result Display Section should display **0 activities listed!**<br>
Tasks Display Panel and Schedule Display Panel should show no activity.

### Error paths
#### Search invalid date/status
1. Enter `search invalid` in the Command Box.
2. Result Display Section should display **Invalid command format! ...** (usage of search command).<br>
Command Box should turn red.

---

### `mark` command

### Normal paths
#### Mark an activity
1. Enter `list` in the Command Box.
2. Result Display Section should display **Listed all activities**.<br>
Task Display Panel and Schedule Display Panel should display all activities in the current stored file location.
3. Enter `mark 2` in the Command Box.
4. Result Display Section should display **Completed Activity: sad life :(**.<br>
Activity 2 (**sad life :(**) should hide from the Tasks Display Panel

### Error paths
#### Mark invalid index
1. Enter `mark 99` in the Command Box.
2. Result Display Section should display **The activity index provided is invalid**.<br>
Command Box should turn red.

---

### `unmark` command

### Normal paths
#### Unmark an activity
1. Enter `search completed` in the Command Box.
2. Result Display Section should display **5 activities listed!**.<br>
Tasks Display Panel and Schedule Display Panel should show all completed activities.
3. Enter `unmark 1` in the Command Box.
4. Result Display Section should display **Pending Activity: sad life :(**.<br>
Activity 1 (**sad life :(**) should hide from the Schedule Display Panel

### Error paths
#### Unmark invalid index
1. Enter `unmark 99` in the Command Box.
2. Result Display Section should display **The activity index provided is invalid**.<br>
Command Box should turn red.

---

### `clear` command
1. Enter `clear` in the Command Box.
2. Result Display Section should display **Your activities have been removed!**<br>
Tasks Display Panel and Schedule Display Panel should show no activity.
3. Enter `list` in the Command Box.
4. Result Display Section should display **Listed all activities**.<br>
Task Display Panel and Schedule Display Panel should show no activity.

---

### `store` command

### Normal paths
#### Change store location to new file
1. Enter `add This is Remindaroo!` in the Command Box.
2. Result Display Section should display **New activity added: This is Remindaroo!**.<br>
Tasks Display Panel should add on with an activity **This is Remindaroo!** with status **pending**.
3. Enter `store data/newFile.xml` in the Command Box.
4. Result Display Section should display **Remindaroo data have been saved to data/newFile.xml**.<br>
Tasks Display Panel and Schedule Display Panel should show activities in **data/newFile.xml**, which should now contain activities in the previous xml file location (namely **data/remindaroo.xml** and Activity 1 **This is Remindaroo!**).<br>
Status Bar should change current xml file location to **data/newFile.xml**.

#### Change store location to existing file
1. Enter `store data/remindaroo.xml` in the Command Box.
2. Result Display Section should display **Remindaroo data have been saved to data/remindaroo.xml**.<br>
Tasks Display Panel and Schedule Display Panel should show activities in **data/remindaroo.xml** (namely Activity 1 **This is Remindaroo!**)<br>
Status Bar should change current xml file location to **data/remindaroo.xml**.

### Error paths
#### Change store location to non-xml file
1. Create a file named `newText.txt` with content `Hello World!` and place it in the same directory as Remindaroo.
2. Enter `store newText.txt` in the Command Box.
3. Result Display Section should display **Invalid command format! ...** (usage of store command).<br>
Command Box should turn red.

---

### `load` command

### Normal paths
#### Load an existing file
1. Enter `load SampleData.xml` in the Command Box.
2. Schedule Display Panel and Tasks Display Panel should be loaded with sample data. <br>
Result Display Section should display **Remindaroo has loaded data from SampleData.xml**. 

### Error paths
#### Load a nonexistent file
1. Enter `load NonExistentData.xml` in the Command Box.
2. Result Display Section should display **Specified data file does not exist!** 

#### Load a non-xml file
1. Enter `load newText.txt` in the Command Box.
2. Result Display Section should display **Invalid command format! ...** (usage of load command).<br>
Command Box should turn red.

---

### `help` command
1. Enter `help` in the Command Box.
2. A help window with Commands, Usage and Examples should pop up.
3. Close the help window.
4. Result Display Section should display **Opened help window.**

---

### `exit` command
1. Enter `exit` in the Command Box.
2. The application should close.
