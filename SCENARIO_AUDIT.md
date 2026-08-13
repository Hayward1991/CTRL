# CTRL v1 scenario audit

Status legend: PASS = implemented and compiled; VERIFY = requires physical-device test; GAP = implementation incomplete.

| Scenario | Status | Expected behaviour |
|---|---|---|
| School morning | PASS | Mandatory routine protects breakfast/prep and school departure constraints. |
| Monday Harrison pickup | PASS | Protected family commitment outranks flexible tasks. |
| Calendar conflict | PASS | Fixed calendar block cannot be displaced by flexible work. |
| Over-capacity day | PASS | CTRL surfaces Over Capacity instead of silently breaking constraints. |
| Must vs Target | PASS | Must ranks above Target and Someday. |
| Washing dependency | PASS | Follow-on washing step remains blocked until dependency is completed. |
| Cleaning overdue | VERIFY | Scheduled Snitch receiver fires a high-priority cleaning escalation if the task is still unfinished. |
| Finished task has stale alarm | PASS | Receiver suppresses alarms for completed/cancelled/skipped tasks. |
| Start/pause/finish | PASS | Active time excludes paused time and completion records learned duration. |
| Visible countdown | PASS | Active/paused task card reconstructs remaining time from persisted runtime fields. |
| Crash/relaunch | VERIFY | Local state reloads and timer/alarm state reconstructs from persisted timestamps. |
| Phone reboot | VERIFY | Boot receiver rebuilds task alarms; Hey Control does not silently start microphone. |
| Replan/capture | PASS | Every normal replan persists tasks and refreshes scheduled alarms. |
| Calendar permission denied | PASS | Calendar bridge degrades to no external blocks rather than crashing. |
| Offline launch | PASS | Local tasks/routines remain usable; cloud reconciliation failure is non-fatal. |
| Gmail CTRL ingest | VERIFY | Requires first authenticated device/user to exist in Supabase. |
| Primary device | VERIFY | Requires authenticated device registration in Supabase. |
| Hey Control | VERIFY | User-started microphone foreground service compiles, captures commands after “Hey Control”, creates tasks and schedules alarms. |
| Health Connect reader | PASS | Foreground steps/sleep reader and permission definitions compile. |
| Health Connect permission UX | GAP | Explicit Health Connect permission launcher/rationale is still blocked from publication. |
| Notification permission | GAP | User-controlled permission component compiles but is not yet surfaced in a screen due publication guardrail. |
| Update checker | PASS | Life screen checks authenticated Supabase release metadata and surfaces a newer APK URL. |
| Biometric gate | VERIFY | Native biometric gate compiles; needs device enrollment test. |
| Approved launcher icon | PASS | White/black CTRL vector is packaged by CI and the icon-enabled APK compiles. |
| Snitch WhatsApp escalation | GAP | No confirmed recipient/number is configured; v1 currently escalates via high-priority notification only. |
| Weather/live travel provider | GAP | No provider has been implemented; fixed travel assumptions remain conservative/local. |

## Release blockers before calling this a final release

1. Physical-device verification of microphone foreground service, notifications, alarm delivery, reboot recovery, biometric prompt and launcher icon.
2. Health Connect permission/rationale UX if Health Connect is required in the first public build.
3. Notification permission control needs to be surfaced, or permission enabled manually during the device candidate test.
4. Direct WhatsApp Snitch escalation requires an explicit configured recipient if it is required for v1.

## Candidate-build position

The branch is suitable for an on-device candidate test once CI is green. It should not be merged to `main` or labelled final release until the VERIFY items above are exercised on the target Android device.
