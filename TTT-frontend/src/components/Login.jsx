import { useState } from "react";

function Login({ className = "", style, onSubmit, onRegister }) {
  const emptyData = {
    username: "",
    password: "",
  };

  const [data, setData] = useState(emptyData);

  // console.log(data);

  function handleChange(event) {
    console.log(event.target.value);

    let newData = {};
    if (event.target.dataset.index) {
      newData = {
        ...data,
      };

      newData[event.target.dataset.key].splice(
        event.target.dataset.index,
        1,
        event.target.value,
      );

      setData(newData);
    } else {
      newData = {
        ...data,
        [event.target.name]: event.target.value,
      };
      setData(newData);
    }
    console.log(newData);
  }

  async function handleSubmit(e) {
    e.preventDefault();
    // Basic client-side validation
    if (!data.username || !data.password) {
      alert("Please enter username and password");
      return;
    }

    // with POST:
    try {
      const resp = await fetch(`/api/auth/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(data),
      });
      if (resp.ok) {
        alert("Successful login!");
        onSubmit && onSubmit(data);
      } else {
        alert("Failed to log in!");
      }
    } catch (err) {
      alert("Network error during login");
    }
  }

  return (
    <>
      <form className="" onSubmit={handleSubmit}>
        <div
          className={["loginPanel", className].join(" ").trim()}
          style={style}
        >
          {Object.entries(data).map(([key, value]) => (
            <div key={key} className="edit-input-box">
              <label className="edit-label" htmlFor={key}>
                {key}:
              </label>
              {
                <input
                  className="edit-input"
                  name={key}
                  onChange={handleChange}
                  type={key === "password" ? "password" : "text"}
                  placeholder={value}
                  defaultValue={value}
                />
              }
            </div>
          ))}
          <button className="loginButton" type="submit">
            Log In
          </button>
          <div className="loginRegister">
            <label htmlFor="register">Don't have an account yet?</label>
            <button
              className="loginSecondaryButton"
              type="button"
              key="register"
              onClick={() => onRegister && onRegister()}
            >
              Register
            </button>
          </div>
        </div>
      </form>
    </>
  );
}

export default Login;
