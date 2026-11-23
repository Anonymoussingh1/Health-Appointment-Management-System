# Health Appointment Management System

An Android application developed in Kotlin using modern Android architecture, designed to digitize and streamline healthcare scheduling, patient intake, consultations, and clinic management.

## Problem Statement
Traditional healthcare appointment systems (manual phone calls, physical booking desks, and paper-based record-keeping) suffer from significant inefficiencies:
- **Scheduling Inefficiencies:** Patients often experience double-bookings, missed appointments, and long waiting times due to poor scheduling visibility.
- **Lack of Accessibility:** Patients struggle to easily find doctors by specialty or check availability outside of clinic working hours.
- **Disorganized Records:** Paper prescriptions and manual medical histories are easily lost or hard for patients to track across visits.
- **Administrative Burden:** Clinics face heavy administrative workloads, leading to human error and inefficient appointment workflows.

## Proposed Solution
Our mobile application provides a centralized digital **Health Appointment Management System** designed to streamline the entire healthcare booking lifecycle:
- **Digital Booking:** Patients can browse doctors by specialty, check availability, and book consultations instantly.
- **Appointment Management:** Real-time tracking of upcoming, completed, and cancelled appointments.
- **Secure Persistence:** Reliable local data storage via SQLite database operations.
- **Organized Records:** Easy access to medical history, consultation details, and pre-visit intake forms.

## Key Features & Modules

### 1. Authentication & Role Management
- Secure Login & Registration with role selection (Patient vs. Doctor).
- Show/Hide password toggle on all password fields.
- Forgot Password / Password Reset link simulation for both patients and doctors.
- Doctor-specific profile setup: Hospital / Clinic selection and Profession / Specialty (e.g., Back Doctor, Eye, Cardiologist).

### 2. Patient Portal
- **Dashboard:** Overview of active appointments, status counters, and quick service shortcuts.
- **Appointment Booking:** Select registered doctors by specialty, pick available dates/times, and state reasons for visits.
- **Digital Intake Forms:** Complete pre-visit health questionnaires (symptoms, allergies, medications) sent directly to the doctor's review history.
- **Prescriptions & Refill Requests:** View active prescriptions and send refill requests directly to the treating doctor.
- **Billing & Copays:** Track consultation copays and process payments.
- **Care Messages:** Secure direct messaging with the clinic and care team.
- **Family & Dependents:** Manage linked family member profiles.
- **Insurance & Info:** View and update health insurance details.

### 3. Doctor Portal
- **Dashboard:** Overview of assigned patient appointments with status breakdowns.
- **Schedule & Shifts:** Manage working hours and block time off / vacations.
- **Waiting Room:** Real-time patient check-ins and appointment requests.
- **Patient Medical History:** Review patient profiles, chief complaints, and submitted intake questionnaires.
- **E-Prescriptions & Lab Orders:** Select patient recipients from a dynamic dropdown menu and issue e-prescriptions or lab test orders.
- **Incoming Refill Requests:** Real-time notification list of prescription refill requests submitted by patients.

### 4. Admin Portal
- Admin Dashboard for centralized management.
- Add and remove Doctors and Patients.
- Search across all registered users.
- View and manage all system appointments (cancel or delete).

## Architecture
- **UI Layer (`ui/`):** Activities, Fragments, and ViewModels managing views and business logic.
- **Data Layer (`data/`):** Repositories, Directory helpers, and SQLite database storage.

## Team
| Stage | Member | Admission No. |
|---|---|---|
| 1 Lead | Aman Singh Bhamra | 165553 |
| 2 ViewModel | Moses Bill Odhiambo | 192092 |
| 3 Room | Ruweidha Mohamed Somo | 189240 |
| 4 Screens | Lee Mutua Wambua | 193772 |
| 5 Data integration | Alia Baba Grace | 177571 |
| 6 Release | Aman Singh Bhamra | 165553 |

## Future Enhancements
- **M-Pesa Payment Integration:** Enable mobile money payments for consultation copays and outstanding clinic bills directly through Safaricom M-Pesa STK push.
- **Push Notifications:** Firebase Cloud Messaging (FCM) integration for real-time appointment reminders, status updates, and prescription notifications.
- **Telehealth Video Consultations:** Built-in WebRTC / Agora video calling for virtual doctor-patient appointments.
- **Automated SMS Reminders:** Twilio / Africa's Talking integration to send automated SMS alerts 24 hours before scheduled visits.
- **Advanced Electronic Health Records (EHR):** Comprehensive diagnostic imaging and lab result file attachments.
