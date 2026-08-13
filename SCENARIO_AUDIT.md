# CTRL v1 scenario audit

Status legend: PASS = implemented and compiled; VERIFY = requires device test; BLOCKED = connector/package blocker; GAP = implementation incomplete.

| Scenario | Status | Expected behaviour |
|---|---|---|
| School morning | PASS | Mandatory routine protects breakfast/prep and school departure constraints. |
| Monday Harrison pickup | PASS | Protected family commitment outranks flexible tasks. |
| Calendar conflict | PASS | Fixed calendar block cannot be displaced by flexible work. |
| Over-capacity day | PASS | CTRL surfaces Over Capacity instead of silently breaking constraints. |
| Must vs Target | PASS | Must ranks above Target and Someday. |
| Washing dependency | PASS | Follow-on washing step remains blocked until dependency is completed. |
| Cleaning overdue | VERIFY | Scheduled Snitch receiver fires an important cleaning notification on device. |
| Start/pause/finish | VERIFY | State persists locally and completion records learned duration. |
| Crash/relaunch | VERIFY | Local state reloads and alarms are reconstructed from persisted timestamps. |
| Phone reboot | VERIFY | Boot receiver rebuilds task alarms; Hey Control does not silently start microphone. |
| Calendar permission denied | PASS | Calendar bridge degrades to no external blocks rather than crashing. |
| Offline launch | PASS | Local tasks/routines remain usable; cloud reconciliation failure is non-fatal. |
| Gmail CTRL ingest | VERIFY | Requires first authenticated device/user to exist in Supabase. |
| Primary device | VERIFY | Requires authenticated device registration in Supabase. |
| Hey Control | VERIFY | Foreground recognizer service compiles and is user-controlled; needs physical-device microphone test. |
| Health Connect steps/sleep | GAP | SDK availability bridge exists; protected health-data reads are not wired yet. |
| Update checker | VERIFY | Release table/query exists; requires authenticated user and a populated app_releases row. |
| Biometric gate | VERIFY | Native biometric gate compiles; needs device enrollment test. |
| Approved launcher icon | BLOCKED | Vector prepared, but connector currently blocks adding the resource into the Android module. |

## Release blockers

1. Health Connect read flow for steps/sleep.
2. Approved launcher icon packaging.
3. Physical-device verification of microphone foreground service, alarm delivery, reboot recovery, biometric prompt and notification permissions.
4. Runtime pause/resume accounting should exclude paused time before release.
