export default function Button({ children, href, className, onClick }) {
  const base = "px-9 py-2 text-2xl font-bold rounded-md max-w-fit cursor-pointer";

  if (onClick) {
    return (
      <button type="button" onClick={onClick} className={`${base} ${className}`}>
        {children}
      </button>
    );
  }

  return (
    <a href={href} className={`${base} ${className}`}>
      {children}
    </a>
  );
}