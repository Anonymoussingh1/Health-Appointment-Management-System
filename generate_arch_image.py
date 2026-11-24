from PIL import Image, ImageDraw, ImageFont

# Create image
img = Image.new("RGB", (1200, 750), color=(244, 247, 251))
draw = ImageDraw.Draw(img)

# Try to load a font, fallback to default if not found
try:
    font_title = ImageFont.truetype("arial.ttf", 24)
    font_header = ImageFont.truetype("arial.ttf", 18)
    font_body = ImageFont.truetype("arial.ttf", 14)
except:
    font_title = ImageFont.load_default()
    font_header = ImageFont.load_default()
    font_body = ImageFont.load_default()

# Draw Title
draw.text((40, 30), "Health Appointment Management System — MVVM Architecture & Data Flow", fill=(13, 71, 161), font=font_title)

# Draw Boxes
def draw_box(box, title, items, fill_color=(255, 255, 255), outline_color=(33, 150, 243)):
    x1, y1, x2, y2 = box
    draw.rectangle([x1, y1, x2, y2], fill=fill_color, outline=outline_color, width=2)
    draw.rectangle([x1, y1, x2, y1 + 40], fill=outline_color)
    draw.text((x1 + 15, y1 + 10), title, fill=(255, 255, 255), font=font_header)

    y_offset = y1 + 50
    for item in items:
        draw.text((x1 + 15, y_offset), f"• {item}", fill=(55, 71, 79), font=font_body)
        y_offset += 25

# Layer 1: UI Layer
draw_box((50, 100, 350, 320), "1. UI Layer (Fragments)", [
    "Patient Portal (Booking, Intake)",
    "Doctor Portal (Waiting Room, E-Rx)",
    "Admin Dashboard (User Management)",
    "Auth Screens (Login, Register)"
], fill_color=(255, 255, 255), outline_color=(21, 101, 192))

# Layer 2: ViewModel Layer
draw_box((430, 100, 750, 320), "2. ViewModel Layer", [
    "AuthViewModel (Session & Roles)",
    "AppointmentViewModel (LiveData)",
    "AdminViewModel (User CRUD)",
    "Lifecycle-aware state retention"
], fill_color=(255, 255, 255), outline_color=(46, 125, 50))

# Layer 3: Data & Directory Helpers
draw_box((810, 100, 1150, 320), "3. Data & Directory Helpers", [
    "UserDirectory (SQLite Users)",
    "IntakeDirectory (Patient Forms)",
    "PrescriptionDirectory (Refills)",
    "Coroutines background threads"
], fill_color=(255, 255, 255), outline_color=(230, 81, 0))

# Layer 4: Persistence Layer
draw_box((300, 420, 900, 650), "4. Persistence Layer (SQLite Database)", [
    "AppDatabase / SQLiteOpenHelper",
    "Users Table (Patients, Doctors, Admins with Specialties & Hospitals)",
    "Appointments Table (Status, Date, Time, Reason)",
    "Local Persistence ensuring zero data loss across app restarts"
], fill_color=(255, 255, 255), outline_color=(106, 27, 154))

# Draw Arrows
def draw_arrow(start, end, label=""):
    x1, y1 = start
    x2, y2 = end
    draw.line([x1, y1, x2, y2], fill=(100, 100, 100), width=3)
    # arrowhead
    draw.polygon([x2, y2, x2-10, y2-5, x2-10, y2+5], fill=(100, 100, 100))
    if label:
        draw.text(((x1+x2)//2 - 20, (y1+y2)//2 - 15), label, fill=(80, 80, 80), font=font_body)

draw_arrow((350, 210), (430, 210), "Observes")
draw_arrow((750, 210), (810, 210), "Queries")
draw_arrow((590, 320), (590, 420), "Persists")

# Save image
img.save("architecture_diagram.png")
print("Architecture image generated successfully!")
