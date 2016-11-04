# User Guide
## 1. Table of Contents

1. [Table of Contents](#1-table-of-contents)
2. [About](#2-about)
3. [Getting Started](#3-getting-started)
4. [Commands](#4-commands)
5. [Summary of Commands](#5-summary-of-commands)
6. [Troubleshooting](#6-troubleshooting)

<!-- @@author A0144704L -->
## 2. About

Remindaroo is a customized to-do list application that can help you manage your busy schedule and organize your to-dos. Whether its working on a project, buying groceries or planning a holiday, Remindaroo aims to help you get stuff done. So let's get started!

## 3. Getting Started

1. Ensure you have Java version `1.8.0_60` or later installed in your Computer.<br>
   > Having any Java 8 version is not enough. <br>
   This app will not work with earlier versions of Java 8.

2. Download the latest `remindaroo.jar` from the [releases](../../../releases) tab.
3. Copy the file to the folder you want to use as the home folder for Remindaroo.
4. Double-click the file to start the app. The Graphical User Interface (GUI) should appear in a few seconds.

> <img src="images/screenshots/Ui.png"><br>
> Figure 1: GUI of Remindaroo

5. Type the command in the command box and press <kbd>Enter</kbd> to execute it. <br>
   e.g. type **`help`** and press <kbd>Enter</kbd> to open the help window.
6. Some examples of commands you can try:
   * **`add`**`buy bread` : adds a task named `buy bread` to Remindaroo
   * **`exit`** : exits the app
7. Refer to the [Commands](#4-commands) section below for details of each command.<br>

<!-- @@author A0139797E -->
## 4. Commands

> Format notations:
>
> * Words in `UPPER_CASE` are the parameters
> * Items in `[SQUARE_BRACKETS]` are optional
> * Keywords with `|` in between indicates that either one can be used
> * Order of parameters are fixed
> * Keywords are case-insensitive

<br>

> Special note on Date and Time parameters:
>
> `DATE_TIME`, `START_DATE_TIME` and `END_DATE_TIME` are parameters that can accept various inputs indicating a specific date or time. Some examples are as follows
> * Date, e.g. `29 Oct`
> * Date and time, e.g. `10 Nov 09:00`
> * Relative day, e.g. `tomorrow`
> * Specific weekday, e.g. `wednesday`
>

> Note: If the timing is not specified, it will be set as the current timing.
> Note: START_DATE_TIME must be earlier than or equal to END_DATE_TIME.

### <br>4.1 Add New Activity : **`add`**
Adds an activity into Remindaroo. 3 types of activities are available:
* _task_ : one without specific time
* _deadline_ : one with just a specific time
* _event_ : one with a start time and end time

##### Formats:<br><br>
-  `add TASK`<br>

	> Example: <br>
	> `add buy milk` <br>
	>
	> Screenshot: <br>
	> <img src="images/screenshots/add_task_after.png"><br><br>
	> Figure 2: After entered command `add buy toilet paper`<br><br>

-  `add DEADLINE on|by DATE_TIME`<br>

	> Example: <br>
	> `add Submit Assignment 1 on 23 Oct` <br>
	> `add Submit Assignment 1 by 23 Oct 09:00` <br>
	>
	> Screenshot: <br>
	> <img src="images/screenshots/add_deadline_after.png"><br><br>
	> Figure 3: After entered command `add project submission by next tues 23:59`<br><br>

- `add EVENT from START_DATE_TIME to END_DATE_TIME`<br>

	> Example: <br>
	> `add Football Tournament from 21 Oct 08:00 to 23 Oct 22:00` <br>
	> `add Football Tournament from 21 Oct to 23 Oct` <br>
	>
	> Screenshot: <br>
	> <img src="images/screenshots/add_event_after.png"><br><br>
	> Figure 4: After entered command `add football competition from 21st nov 9am to 21st nov 5pm`<br><br>

<!-- @@author A0135730M -->
##### Note:<br><br>
-	Recurring deadlines / events can be added using just an `add` command, by appending ` for RECUR_NUM TIME_UNIT` to it, where `RECUR_NUM` is any positive integer, and `TIME_UNIT` is either `day`, `week`, `month`, `year` (case-insensitive, accepts both singular and plural form).
	> Example: <br>
	> `add transfer window closing on 31 oct for 3 years` <br>
	> `add monthly meeting from 1 Oct 1pm to 1 Oct 3pm for 5 months` <br>
	>
	> Screenshot: <br>
	> <img src="images/screenshots/add_recurring_after.png"><br><br>
	> Figure 5: After entered command `add afternoon run from today 5pm to today 6pm for 5 days`<br><br>

-  If activity name contains keywords, add quotation marks to the keywords that separate the name and time.
	> Example: <br>
	> `add read All by Me "by" 23 Oct` <br>
	> `add screening from Tokyo to Paris "from" 21 Oct "to" 23 Oct` <br>

<!-- @@author A0144881Y -->
### <br>4.2 List all Activities : **`list`**
Displays all activities in Remindaroo

##### Format:
- `list`

### <br>4.3 Clear all Activities : **`clear`**
Clear all activities in Remindaroo

##### Format:
- `clear`

### <br>4.4 Delete Activity : **`delete`**
Deletes a specific activity from Remindaroo

##### Format:<br><br>
- `delete ACTIVITY_ID`<br>

	> Example: <br>
	> `delete 1` <br>
	> Activity with ID 1 (e.g. `Football Practice`) is deleted from the Remindaroo
	>
	> Screenshot: <br>
	> <img src="images/screenshots/delete_before.png"><br><br>
	> Figure 6: Command to type in order to delete event "Project Meeting"<br><br>
	>
	> <img src="images/screenshots/delete_after.png"><br><br>
	> Figure 7: After entered command `delete 3`<br><br>

### <br>4.5 Update Activity : **`update`**
Updates name, date and/or time of specific activity to specified name, date and/or time.

##### Format:<br><br>
-  `update ACTIVITY_ID [NEW_NAME] from [DATE_TIME] to [END_DATE_TIME]`<br>

	> Examples:<br>
	> `update 1 buy bread`<br>
	> Activity with ID 1 is updated to `buy bread`<br>
	>
	> `update 2 on 10 Oct 10:00`<br>
	> Activity with ID 2 is updated to be on / due by 10 October, 10:00<br>
	>
	> `update 3 from 11 Oct 13:00 to 11 Oct 14:00`<br>
	> Activity with ID 3 is updated to be on 11 October, 13:00 to 14:00<br>
	>
	> Screenshot: <br>
	> <img src="images/screenshots/update_event_before.png"><br><br>
	> Figure 8: Command to type in order to update event "football competition"<br><br>
	>
	> <img src="images/screenshots/update_event_after.png"><br><br>
	> Figure 9: After entered command `update 9 football tournament from 21 nov 9am to 23 nov 5pm`<br><br>


<!-- @@author A0144704L -->
### <br>4.6 Mark Activity : **`mark`**
Marks an activity as completed. Displays remaining pending activities.

##### Format:<br><br>
- `mark ACTIVITY_ID`<br>

	> Example:<br>
	> `mark 1`<br>
	> Activity with ID 1 (e.g. `do assignment 1`) is marked as completed
	>
	> Screenshot: <br>
	> <img src="images/screenshots/mark_before.png"><br><br>
	> Figure 10: Command to type in order to mark task "buy toilet paper"<br><br>
	>
	> <img src="images/screenshots/mark_after.png"><br><br>
	> Figure 11: After entered command `mark 12`<br><br>

### <br>4.7 Unmark Activity : **`unmark`**
Unmarks an activity as pending. Displays remaining pending activities.

##### Format:<br><br>
- `unmark ACTIVITY_ID`<br>

	> Example:<br>
	> `unmark 1`<br>
	> Activity with ID 1 (e.g. `do assignment 1`) is unmarked as pending

<!-- @@author A0135730M -->
### <br>4.8 Search for Activities : **`search`**
Displays list of activities that match description (keywords / date / status)
If search by keywords, add quotation marks (either ' or ") around keywords.

##### Formats:<br><br>
- `search KEYWORDS`<br>

	> Example:<br>
	> `search "CS2101 tutorial"`<br>
	> `search 'Assignment 1'`<br>
	>
	> Screenshot: <br>
	> <img src="images/screenshots/search_keywords_before.png"><br><br>
	> Figure 12: Command to type in order to search for activity with "assignment"<br><br>
	>
	> <img src="images/screenshots/search_keywords_after.png"><br><br>
	> Figure 13: After entered command `search 'assignment'`<br><br>

- `search DATE_TIME [to END_DATE_TIME]`<br>

	> Example:<br>
	> `search today`<br>
	> `search 21 Oct to 23 Oct`<br>
	>
	> Screenshot: <br>
	> <img src="images/screenshots/search_date_before.png"><br><br>
	> Figure 14: Command to type in order to search for activities between today (Wednesday) and Friday<br><br>
	>
	> <img src="images/screenshots/search_date_after.png"><br><br>
	> Figure 15: After entered command `search today to fri`<br><br>

- `search STATUS`<br>

    > Example:<br>
    > `search pending`<br>
    > `search completed`<br>

<!-- @@author A0139797E -->
### <br>4.9 Undo last command : **`undo`**
Undoes last N command(s) entered (default N equals 1).

##### Format:<br><br>
- `undo [NUM_OF_TIMES]`<br>

	>  Example:<br>
	>  `add CS2101 Tutorial`<br>
	>  `add CS2103 Tutorial`<br>
	>  `undo 2`<br>
	>  CS2101 tutorial and CS2103 Tutorial are removed from Remindaroo
	>
	> Screenshot: <br>
	> <img src="images/screenshots/undo_before.png"><br><br>
	> Figure 16: Command to type in order to undo previous command<br><br>
	>
	> <img src="images/screenshots/undo_after.png"><br><br>
	> Figure 17: After entered command `undo`<br><br>

### <br>4.10 Redo last command: **`redo`**
Redoes last N undo command(s) (default N equals 1).

##### Format:<br><br>

- `redo [NUM_OF_TIMES]`<br>

	>  Example:<br>
	>  `add CS2101 Tutorial`<br>
	>  `add CS2103 Tutorial`<br>
	>  `undo 2`<br>
	>  `redo 2`<br>
	>  CS2101 tutorial and CS2103 Tutorial are re-added to Remindaroo
	>
	> Screenshot: <br>
	> <img src="images/screenshots/redo_after.png"><br><br>
	> Figure 18: After entered command `undo` from Figure 17<br><br>

<!-- @@author A0144704L -->
### <br>4.11 Change Data Storage Location : **`store`**
Changes the location of the data file of Remindaroo to the specified path (relative to the directory of the Remindaroo app).

##### Format:<br>

- `store NEW_DATA_FILE_LOCATION`<br>

	> Example:<br>
	> `store new_data/Remindaroo.xml`<br>
	> The Data file `Remindaroo.xml` now resides in the folder `new_data`
	>
	> Screenshot: <br>
	> <img src="images/screenshots/store_after.png"><br><br>
	> Figure 19: After entered command `store data/otherRemindaroo.xml`<br><br>

### <br>4.12 Show Help Menu : **`help`**
Displays the help window, which contains instructions for using each of the commands

##### Format:<br><br>

- `help`<br>

	> Screenshot: <br>
	> <img src="images/screenshots/help_after.png"><br><br>
	> Figure 20: After entered command `help`<br><br>

### <br>4.13 Exit Remindaroo : **`exit`**
Exits the program

##### Format:<br><br>
- `exit`

### <br>4.14 Saving the data
ActivityManager data is saved in the hard disk automatically after any command changes the data.
There is no need to save manually.
<br>

<!-- @@author A0144881Y -->
## 5. Summary of Commands

| Commands        | Format        |
| ----------------|:-------------:|
| Add Task | `add TASK` |
| Add Deadline | `add DEADLINE on|by DATE_TIME` |
| Add Event | `add EVENT from START_DATE_TIME to END_DATE_TIME` |
| Add Recurring Deadline / Event | `add DEADLINE_AND_TIME|EVENT_AND_TIME for RECUR_NUM TIME_UNIT` |
| List all Activities | `list` |
| Clear all Activities | `clear` |
| Delete Activity | `delete ACTIVITY_ID`|
| Update Activity | `update ACTIVITY_ID [NEW_NAME] from [DATE_TIME] to [END_DATE_TIME]` |
| Mark Activity   | `mark ACTIVITY_ID` |
| Unmark Activity   | `unmark ACTIVITY_ID` |
| Search Activity | `search KEYWORDS | DATE_TIME [to END_DATE_TIME] | STATUS`
| Undo            | `undo [NUMBER_OF_TIMES]` |
| Redo            | `redo [NUMBER_OF_TIMES]` |
| Change data storage location | `store NEW_DATA_FILE_LOCATION` |
| Help            | `help` |
| Exit            | `exit` |



## 6. Troubleshooting

**Q:** How do I transfer my data to another computer?<br>
**A:** Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous Remindaroo folder.
