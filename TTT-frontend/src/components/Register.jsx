import { useState } from "react";
import { useAudio } from "../hooks/useAudio";
import { api } from "../state/config";

export default function Register({ className = "", style, onBack, onSubmit }) {
    const { play } = useAudio();
  const emptyData = {
      ["birth-date"]: "",
    username: "",
    email: "",
    password: ""
  };
  const [data, setData] = useState(emptyData);

  function handleChange(event) {
    play('type');
    const { name, value } = event.target;
    if (name === 'confirm') return;
    setData((prev) => ({ ...prev, [name]: value }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    if (
      !data.username ||
      !data.email ||
      !data.password ||
      !data["birth-date"]
    ) {
      alert("Please fill all fields");
      return;
    }

    const formData = new FormData(e.target);
    const confirm = formData.get('confirm') || '';
    if (data.password !== confirm) {
      alert("Passwords do not match");
      return;
    }

    try {
      const resp = await fetch(api(`/user/register`), {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username: data.username,
          email: data.email,
          password: data.password,
          birthDate: data["birth-date"],
        }),
      });
      if (resp.ok) {
        alert("Registration successful");
        onSubmit && onSubmit({ username: data.username, email: data.email });
      } else {
        const msg = await resp.text().catch(() => "");
        alert(`Registration failed (${resp.status}). ${msg || ""}`);
      }
    } catch (err) {
      alert("Network error during registration");
    }
  }

  return (
    <form className="" onSubmit={handleSubmit}>
      <div className={["loginPanel", className].join(" ").trim()} style={style}>
        {Object.entries(emptyData).map(([key]) => (
          <div key={key} className="edit-input-box">
            <label className="edit-label" htmlFor={key}>
              {key}:
            </label>
              <input
                className={key.includes("date") ? "edit-date" : "edit-input"}
                name={key}
                onChange={handleChange}
                type={
                  key === "password" ? "password"
                    : key === "email" ? "email"
                    : key.includes("date") ? "date" 
                    : "text"
                }
                placeholder=""
                defaultValue={key === "birth-date" ? "2000-01-01" : data[key]}
              />
          </div>
        ))}
        <div key={"confirm"} className="edit-input-box">
          <label className="edit-label" htmlFor={"confirm"}>
            confirm password:
          </label>
            <input
              className="edit-input"
              name={"confirm"}
              onChange={handleChange}
              type={"password"}
              placeholder=""
            />
        </div>
          <button className="loginButton" type="submit">
            Create Account
          </button>
          <button
            className="loginButton"
            type="button"
            onClick={() => onBack && onBack()}
          >
            Back
          </button>
        </div>
    </form>
  );
}
