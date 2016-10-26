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
4. Double-click the file to start the app. The GUI should appear in a few seconds.

4. Type the command in the command box and press <kbd>Enter</kbd> to execute it. <br>
   e.g. type **`help`** and press <kbd>Enter</kbd> to open the help window.
5. Some examples of commands you can try:
   * **`add`**`buy bread` : adds a task named `buy bread` to Remindaroo
   * **`exit`** : exits the app
6. Refer to the [Commands](#4-commands) section below for details of each command.<br>

<!-- @@author A0139797E -->
## <br> 4. Commands

> Format notations:
>
> * Words in `UPPER_CASE` are the parameters
> * Items in `[SQUARE_BRACKETS]` are optional
> * Keywords with `|` in between indicates that either one can be used
> * Order of parameters are fixed

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
* _floating task_ : one without specific time
* _deadline_ : one with just a specific time
* _event_ : one with a start time and end time

##### Formats:<br><br>
-  `add FLOATING_TASK`<br>

	> Example: <br>
	> `add buy milk` <br>
-  `add DEADLINE on|by DATE_TIME`<br>

	> Example: <br>
	> `add Submit Assignment 1 on 23 Oct` <br>
	> `add Submit Assignment 1 by 23 Oct 09:00` <br>

- `add EVENT from START_DATE_TIME to END_DATE_TIME`<br>

	> Example: <br>
	> `add Football Tournament from 21 Oct 08:00 to 23 Oct 22:00` <br>
	> `add Football Tournament from 21 Oct to 23 Oct` <br>

Note: If activity name contains keywords, add quotation marks to the keywords that separate the name and time.
> Example: <br>
> `add read All by Me "by" 23 Oct` <br>
> `add screening from Tokyo to paris "from" 21 Oct "to" 23 Oct`

### <br>4.2 View Activities : **`view`**
Displays the details of the activity / activities<br><br>

- `add EVENT from START_DATE_TIME to END_DATE_TIME`<br>

	> Example: <br>
	> `add Football Tournament from 21 Oct 08:00 to 23 Oct 22:00` <br>
	> `add Football Tournament from 21 Oct to 23 Oct` <br>

<!-- @@author A0135730M -->
##### Note:<br><br>
-	Recurring deadlines / events can be added using just an `add` command, by appending ` for RECUR_NUM TIME_UNIT` to it, where `RECUR_NUM` is any positive integer, and `TIME_UNIT` is either `day`, `week`, `month`, `year` (case-insensitive, accepts both singular and plural form).
	> Example: <br>
	> `add transfer window closing on 31 oct for 3 years` <br>
	> `add monthly meeting from 1 Oct 1pm to 1 Oct 3pm for 5 months` <br>

-  If activity name contains keywords, add quotation marks to the keywords that separate the name and time.
	> Example: <br>
	> `add read All by Me "by" 23 Oct` <br>
	> `add screening from Tokyo to Paris "from" 21 Oct "to" 23 Oct` <br>

-  Keywords in `add` command are case-insensitive.
	> Example: <br>
	> `add Submit Assignment BY today` <br>
	> `add Birthday Party From Today 7pm To Tomorrow 12am` <br>

### <br>4.2 List all Activities : **`list`**
Displays all activities in Remindaroo

##### Format:
- `list`

### <br>4.3 Clear all Activities : **`clear`**
Clear all activities in Remindaroo

##### Format:
- `clear`

<!-- @@author A0144881Y -->
### <br>4.4 Delete Activity : **`delete`**
Deletes a specific activity from Remindaroo

##### Format:<br><br>
- `delete ACTIVITY_ID`<br>

	> Example: <br>
	> `delete 1` <br>
	> Activity with ID 1 (e.g. `Football Practice`) is deleted from the Remindaroo

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


<!-- @@author A0144704L -->
### <br>4.6 Mark Activity : **`mark`**
Marks an activity with status. If the activity already has a status, it will be overwritten by the status in this command.

Status has to be either **pending** or **completed** (case-insensitive).

##### Format:<br><br>
- `mark ACTIVITY_ID as STATUS`<br>

	> Example:<br>
	> `mark 1 as completed`<br>
	> Activity with ID 1 (e.g. `do assignment 1`) is marked as completed

### <br>4.7 Find Next Activity : **`next`**
Displays the activity that is scheduled next (closest to current time)

##### Format:<br><br>
- `next`

<!-- @@author A0135730M -->
### <br>4.8 Search for Activities : **`search`**
Displays list of activities that match description (keyword / date / activity category / status)

##### Formats:<br><br>
- `search KEYWORDS`<br>

	> Example:<br>
	> `search CS2101 tutorial`<br>

- `search DATE_TIME [to END_DATE_TIME]`<br>

	> Example:<br>
	> `search today`<br>
	> `search 21 Oct to 23 Oct`<br>

- `search STATUS`<br>

    > Example:<br>
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

<!-- @@author A0144704L -->
### <br>4.11 Change Data Storage Location : **`store`**
Changes the location of the data file of Remindaroo to the specified path (relative to the directory of the Remindaroo app).

##### Format:<br>

- `store NEW_DATA_FILE_LOCATION`<br>

	> Example:<br>
	> `store /new_data/Remindaroo.xml`<br>
	> The Data file `Remindaroo.xml` now resides in the folder `./new_data`

### <br>4.12 Show Help Menu : **`help`**
Displays the instruction for using each command

##### Format:<br><br>

- `help [COMMAND]`<br>

	> Example: <br>
	>
	> `help | help SOME_INVALID_COMMAND`<br>
	> Displays instruction for all commands<br>
	> `help add`<br>
	> Displays instruction for add command

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
| Mark Activity   | `mark ACTIVITY_ID as STATUS` |
| Next Activity   | `next` |
| Search Activity | `search KEYWORDS | DATE_TIME [to END_DATE_TIME] | STATUS`
| Undo            | `undo [NUMBER_OF_TIMES]` |
| Redo            | `redo [NUMBER_OF_TIMES]` |
| Change data storage location | `store NEW_DATA_FILE_LOCATION` |
| Help            | `help [COMMAND]` |
| Exit            | `exit` |



## 6. Troubleshooting

**Q:** How do I transfer my data to another computer?<br>
**A:** Install the app in the other computer and overwrite the empty data file it creates with the file that contains the data of your previous Remindaroo folder.
