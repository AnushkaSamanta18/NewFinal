

document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("form");

    form.addEventListener("submit", (event) => {
      let hasError = false;

      const fields = form.querySelectorAll("input, textarea, select");

      fields.forEach((field) => {
        if (field.value.trim() === "") {
          hasError = true;
        }
      });

      if (hasError) {
        event.preventDefault(); // Prevent form submission
        alert("Fill up all fields.");
      } else {
        event.preventDefault(); // Simulate form submission
        alert("Booking confirmed!");
      }
    });
  });

  function validateField() {
    let inputField = document.getElementById("charField");
    let errorMessage = document.getElementById("error");
    
    if (inputField.value.length !== 14) {
        errorMessage.textContent = "Error: Input must be exactly 14 characters.";
errorMessage.style.color = "red";
    } else {
        errorMessage.textContent = "";
    }
}
 
document.addEventListener("DOMContentLoaded", function() {
    let inputField = document.getElementById("charField");
    inputField.addEventListener("input", validateField);
});
