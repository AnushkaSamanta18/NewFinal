document.addEventListener('DOMContentLoaded', function() {
    const adminDetails = {
        name: "ANUSHKA SAMANTA",
        phone: "7074105580",
        email: "anushka@gmail.com"
    };

    document.getElementById('adminname').textContent = adminDetails.name;
    document.getElementById('adminPhone').textContent = adminDetails.phone;
    document.getElementById('adminEmail').textContent = adminDetails.email;
});
